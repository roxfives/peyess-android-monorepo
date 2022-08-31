package com.peyess.salesapp.feature.sale.payment.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.clients.ClientRepository
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
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
    }

    private fun loadPaymentMethods() = withState {
        paymentMethodRepository
            .payments()
            .execute(Dispatchers.IO) {
                Timber.i("Loading payment methods $it")
                copy(paymentMethodsAsync = it)
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

                // TODO: Add frames value after converting to double
                val framesValue = if (frames.areFramesNew) {
                    0.0
                } else {
                    0.0
                }

                // TODO: Update coloring and treatment to use price instead of suggested price
                var totalToPay = lens.price //+ frames.value

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

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PaymentViewModel, PaymentState> {
        override fun create(state: PaymentState): PaymentViewModel
    }

    companion object:
        MavericksViewModelFactory<PaymentViewModel, PaymentState> by hiltMavericksViewModelFactory()
}