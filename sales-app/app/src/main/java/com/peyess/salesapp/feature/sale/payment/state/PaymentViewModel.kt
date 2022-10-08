package com.peyess.salesapp.feature.sale.payment.state

import android.net.Uri
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.sale.PaymentMethodType
import com.peyess.salesapp.utils.products.ProductSet
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

class PaymentViewModel @AssistedInject constructor(
    @Assisted initialState: PaymentState,
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
): MavericksViewModel<PaymentState>(initialState) {

    init {
        loadTotalPaid()
        loadTotalToPay()
        loadPaymentMethods()

        onEach(PaymentState::totalPaidAsync, PaymentState::totalToPayAsync) { paid, toPay ->
            if (paid is Success && toPay is Success) {
                setState {
                    copy(
                        totalLeftToPay = (toPay.invoke() - paid.invoke())
                            .coerceAtLeast(0.0)
                    )
                }
            }
        }

        onEach(PaymentState::totalLeftToPay) {
            if (it <= 0.0) {
                // TODO: update payment to be at most totalLeftToPay
            }
        }

        onEach(PaymentState::paymentAsync, PaymentState::clientAsync) { payment, client ->
            val pay = payment.invoke()
            val payer = client.invoke()

            if (
                payment is Success
                    && client is Success
                    && pay != null
                    && payer != null
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    saleRepository.updatePayment(
                        pay.copy(
                            clientId = payer.id,
                            clientDocument = payer.document,
                            clientName = payer.name,
                            clientAddress = payer.shortAddress,
                            clientPicture = payer.picture,
                        )
                    )
                }
            }
        }

        onEach(PaymentState::payment) { payment ->
            withState {
                val minInstallments = 1
                val maxInstallments = it.activePaymentMethod?.maxInstallments ?: minInstallments
                val installments = if (it.activePaymentMethod?.hasInstallments == true) {
                    payment.installments.coerceAtMost(maxInstallments)
                } else {
                    minInstallments
                }

                viewModelScope.launch(Dispatchers.IO) {
                    saleRepository.updatePayment(
                        payment.copy(
                            installments = installments
                        )
                    )
                }
            }
        }
    }

    private fun loadPaymentMethods() = withState {
        paymentMethodRepository
            .payments()
            .execute(Dispatchers.IO) {
                Timber.i("Loading payment methods $it")
                val payments = it.invoke() ?: emptyList()

                if (it is Success) {
                    copy(
                        paymentMethodsAsync = it,
                        activePaymentMethod = if (payments.isNotEmpty()) payments[0] else null
                    )
                } else {
                    copy(paymentMethodsAsync = it)
                }
            }
    }

    private fun loadTotalPaid() = withState {
        saleRepository
            .payments()
            .map {
                var totalPaid = 0.0

                it.forEach { payment ->
                    totalPaid += payment.value
                }

                Timber.i("Loaded total paid: $totalPaid")
                totalPaid
            }
            .execute(Dispatchers.IO) {
                copy(totalPaidAsync = it)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadTotalToPay() = withState {
        saleRepository
            .pickedProduct()
            .filterNotNull()
            .flatMapLatest {
                combine(
                    productRepository.lensById(it.lensId).filterNotNull().take(1),
                    productRepository.coloringById(it.coloringId).filterNotNull().take(1),
                    productRepository.treatmentById(it.treatmentId).filterNotNull().take(1),
                    saleRepository.currentFramesData(),
                    ::ProductSet
                )
            }
            .map {
                val lens = it.lens
                val coloring = it.coloring
                val treatment = it.treatment
                val frames = it.frames

                val framesValue = if (frames.areFramesNew) {
                    frames.value
                } else {
                    0.0
                }

                // TODO: Update coloring and treatment to use price instead of suggested price
                var totalToPay = lens.price + framesValue

                if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
                    totalToPay += coloring.suggestedPrice
                }
                if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
                    totalToPay += treatment.suggestedPrice
                }

                if (coloring.suggestedPrice > 0) {
                    totalToPay += lens.suggestedPriceAddColoring
                }
                if (treatment.suggestedPrice > 0) {
                    totalToPay += lens.suggestedPriceAddTreatment
                }

                totalToPay
            }
            .execute(Dispatchers.IO) {
                copy(totalToPayAsync = it)
            }
    }

    fun loadClient(clientId: String) = withState {
        clientRepository
            .clientById(clientId)
            .execute(Dispatchers.IO) {
                Timber.i("Loading client ($clientId) $it")

                copy(clientAsync = it)
            }
    }

    fun loadPayment(paymentId: Long) = withState {
        saleRepository
            .paymentById(paymentId)
            .execute(Dispatchers.IO) {
                copy(paymentAsync = it)
            }
    }

    fun onTotalPaidChange(value: Double) = withState {
        val paid = value.coerceAtMost(it.payment.value + it.totalLeftToPay)

        saleRepository.updatePayment(it.payment.copy(value = paid))
    }

    fun onIncreaseInstallments(value: Int) = withState {
        val maxInstallments = it.activePaymentMethod?.maxInstallments ?: 1
        val newValue = (value + 1).coerceAtMost(maxInstallments)

        saleRepository.updatePayment(it.payment.copy(installments = newValue))
    }

    fun onDecreaseInstallments(value: Int) = withState {
        val minInstallments = 1
        val newValue = (value - 1).coerceAtLeast(minInstallments)

        saleRepository.updatePayment(it.payment.copy(installments = newValue))
    }

    fun onPaymentMethodChanged(method: PaymentMethod) = setState {
        saleRepository.updatePayment(
            this.payment.copy(
                methodId = method.id,
                methodName = method.name,
            )
        )

        copy(activePaymentMethod = method)
    }

    fun cancelPayment() = withState {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository.deletePayment(it.payment)
        }
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PaymentViewModel, PaymentState> {
        override fun create(state: PaymentState): PaymentViewModel
    }

    companion object:
        MavericksViewModelFactory<PaymentViewModel, PaymentState> by hiltMavericksViewModelFactory()
}