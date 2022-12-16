package com.peyess.salesapp.feature.sale.payment.state

import android.net.Uri
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
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
    private val cardFlagsRepository: CardFlagRepository,
    private val discountRepository: OverallDiscountRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
): MavericksViewModel<PaymentState>(initialState) {

    init {
        loadCurrentSale()

        loadTotalPaid()
        loadTotalToPay()
        loadPaymentMethods()

        loadCardFlagsRepository()

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

//        onAsync(PaymentState::clientAsync) { client ->
//            withState { state ->
//                if (client != null) {
//                    saleRepository.updatePayment(
//                        state.payment.copy(
//                            clientId = client.id,
//                            clientDocument = client.document,
//                            clientName = client.name,
//                            clientAddress = client.shortAddress,
//                            clientPicture = client.picture,
//                        )
//                    )
//                }
//            }
//        }

        onEach(PaymentState::paymentAsync, PaymentState::clientAsync) { payment, client ->
            val pay = payment.invoke()
            val payer = client.invoke()

            Timber.i("UPDATING: Pay async is $payment")
            Timber.i("UPDATING: Payer async is $client")

            Timber.i("UPDATING: Pay is $pay")
            Timber.i("UPDATING: Payer is $payer")

            if (
                payment is Success
                    && client is Success
                    && pay != null
                    && (payer != null && payer.id.isNotBlank())
            ) {
                viewModelScope.launch(Dispatchers.IO) {
                    Timber.i("UPDATING: with pay $pay")
                    Timber.i("UPDATING: with payer $payer")

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

    private fun loadCurrentSale() {
        saleRepository.activeSale().execute {
            copy(saleIdAsync = it)
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
                        activePaymentMethod = if (payments.isNotEmpty()) {
                            val method = payments[0]

                            saleRepository.updatePayment(
                                this.payment.copy(
                                    methodId = method.id,
                                    methodType = method.type,
                                    methodName = method.name,
                                )
                            )

                            method
                        } else {
                            null
                        }
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

    private fun loadCardFlagsRepository() = withState {
        cardFlagsRepository
            .listCards()
            .execute(Dispatchers.IO) {
                copy(cardFlagsAsync = it)
            }
    }

    private fun loadTotalToPay() = withState { state ->
        suspend {
            var pickedProducts: ProductPickedEntity? = null

            var lens = LocalLensEntity()
            var coloring = LocalColoringEntity()
            var treatment = LocalTreatmentEntity()
            var frames = FramesEntity()

            saleRepository
                .pickedProduct()
                .filterNotNull()
                .take(1)
                .collect {
                    pickedProducts = it
                }

            productRepository
                .lensById(pickedProducts?.lensId ?: "")
                .filterNotNull()
                .take(1)
                .collect {
                    lens = it
                }
            productRepository
                .coloringById(pickedProducts?.coloringId ?: "")
                .filterNotNull()
                .take(1)
                .collect {
                    coloring = it
                }
            productRepository
                .treatmentById(pickedProducts?.treatmentId ?: "")
                .filterNotNull()
                .take(1)
                .collect {
                    treatment = it
                }
            saleRepository
                .currentFramesData()
                .filterNotNull()
                .take(1)
                .collect {
                    frames = it
                }

            val framesValue = if (frames.areFramesNew) {
                frames.value
            } else {
                0.0
            }

            val discount = discountRepository
                .getDiscountForSale(state.saleId)
                ?: OverallDiscountDocument()

            val fee = paymentFeeRepository
                .getPaymentFeeForSale(state.saleId)
                ?: PaymentFeeDocument()

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

            val valueWithDiscount = when (discount.discountMethod) {
                DiscountCalcMethod.None -> totalToPay
                DiscountCalcMethod.Percentage -> totalToPay * (1.0 - discount.overallDiscountValue)
                DiscountCalcMethod.Whole -> totalToPay - discount.overallDiscountValue
            }
            val valueWithFee = when (fee.method) {
                PaymentFeeCalcMethod.None -> valueWithDiscount
                PaymentFeeCalcMethod.Percentage -> valueWithDiscount * (1.0 + fee.value)
                PaymentFeeCalcMethod.Whole -> valueWithDiscount + fee.value
            }

            valueWithFee
        }.execute(Dispatchers.IO) {
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

    fun onMethodPaymentChanged(document: String) = withState {
        saleRepository.updatePayment(it.payment.copy(document = document))
    }

    fun onCardFlagChanged(cardFlagIcon: Uri, cardFlagName: String) = withState {
        saleRepository.updatePayment(
            it.payment.copy(
                cardFlagIcon = cardFlagIcon,
                cardFlagName = cardFlagName,
            )
        )
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
                methodType = method.type,
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