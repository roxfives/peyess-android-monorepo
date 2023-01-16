package com.peyess.salesapp.feature.sale.payment.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.payment_method.PaymentMethod
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentRepository
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentTotalResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentUpdateResult
import com.peyess.salesapp.data.repository.local_sale.payment.SinglePaymentResponse
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.feature.sale.payment.adapter.toColoring
import com.peyess.salesapp.feature.sale.payment.adapter.toFrames
import com.peyess.salesapp.feature.sale.payment.adapter.toLens
import com.peyess.salesapp.feature.sale.payment.adapter.toOverallDiscount
import com.peyess.salesapp.feature.sale.payment.adapter.toPayment
import com.peyess.salesapp.feature.sale.payment.adapter.toPaymentFee
import com.peyess.salesapp.feature.sale.payment.adapter.toSalePaymentDocument
import com.peyess.salesapp.feature.sale.payment.adapter.toTreatment
import com.peyess.salesapp.feature.sale.payment.model.Coloring
import com.peyess.salesapp.feature.sale.payment.model.Frames
import com.peyess.salesapp.feature.sale.payment.model.Lens
import com.peyess.salesapp.feature.sale.payment.model.Treatment
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

private typealias ViewModelFactory = AssistedViewModelFactory<PaymentViewModel, PaymentState>
private typealias MavericksVMFactory = MavericksViewModelFactory<PaymentViewModel, PaymentState>

class PaymentViewModel @AssistedInject constructor(
    @Assisted initialState: PaymentState,
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val cardFlagsRepository: CardFlagRepository,
    private val discountRepository: OverallDiscountRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val framesRepository: LocalFramesRepository,
    private val salePaymentRepository: SalePaymentRepository,
): MavericksViewModel<PaymentState>(initialState) {

    init {
//        loadPaymentMethods()

        loadCardFlagsRepository()

//        onEach(PaymentState::paymentAsync, PaymentState::clientAsync) { payment, client ->
//            val pay = payment.invoke()
//            val payer = client.invoke()
//
//            if (
//                payment is Success
//                && client is Success
//                && pay != null
//                && (payer != null && payer.id.isNotBlank())
//            ) {
//                viewModelScope.launch(Dispatchers.IO) {
//                    saleRepository.updatePayment(
//                        pay.copy(
//                            clientId = payer.id,
//                            clientDocument = payer.document,
//                            clientName = payer.name,
//                            clientAddress = payer.shortAddress,
//                            clientPicture = payer.picture,
//                        )
//                    )
//                }
//            }
//        }

        onAsync(PaymentState::productPickedResponseAsync) { processProductPickedResponse(it) }
        onAsync(PaymentState::lensResponseAsync) { processLensPickedResponse(it) }
        onAsync(PaymentState::coloringResponseAsync) { processColoringPickedResponse(it) }
        onAsync(PaymentState::treatmentResponseAsync) { processTreatmentPickedResponse(it) }
        onAsync(PaymentState::framesResponseAsync) { processFramesResponse(it) }

        onAsync(PaymentState::discountAsync) { processDiscountResponse(it) }
        onAsync(PaymentState::paymentFeeAsync) { processPaymentFeeResponse(it) }

        onAsync(PaymentState::totalPaymentResponseAsync) { processTotalPaymentResponse(it) }
        onAsync(PaymentState::paymentResponseAsync) { processPaymentResponse(it) }

        onAsync(PaymentState::paymentUpdateResponseAsync) { processPaymentUpdateResponse(it) }

        onEach(PaymentState::activePaymentMethod) { updatePaymentWithMethod(it) }

        onEach(PaymentState::totalToPay, PaymentState::totalPaid) { totalToPay, totalPaid ->
            updateTotalLeftToPay(totalToPay, totalPaid)
        }

        onEach(PaymentState::paymentId) { watchPaymentDataStream(it) }

        onEach(PaymentState::payment) { payment ->
            withState {
                val minInstallments = 1
                val maxInstallments = it.activePaymentMethod.maxInstallments
                val installments = if (it.activePaymentMethod.hasInstallments) {
                    payment.installments.coerceAtMost(maxInstallments)
                } else {
                    minInstallments
                }

//                viewModelScope.launch(Dispatchers.IO) {
//                    saleRepository.updatePayment(
//                        payment.copy(
//                            installments = installments
//                        )
//                    )
//                }
            }
        }

        onEach(PaymentState::serviceOrderId) {
            loadProductPicked(it)
            loadFrames(it)
        }

        onEach(PaymentState::saleId) {
            loadPaymentFee(it)
            loadDiscount(it)
            loadTotalPaid(it)
        }

        onEach(PaymentState::productPicked) { loadProducts(it) }

        onEach(
            PaymentState::lens,
            PaymentState::coloring,
            PaymentState::treatment,
            PaymentState::frames,
        ) { lens, coloring, treatment, frames ->
            updateTotalToPay(lens, coloring, treatment, frames)
        }
    }

    // Load payment method using coroutines
    // Load the client using coroutines

//    private fun loadPaymentMethods() = withState {
//        paymentMethodRepository
//            .payments()
//            .execute(Dispatchers.IO) {
//                Timber.i("Loading payment methods $it")
//                val payments = it.invoke() ?: emptyList()
//
//                if (it is Success) {
//                    copy(
//                        paymentMethodsAsync = it,
//                        activePaymentMethod = if (payments.isNotEmpty()) {
//                            val method = payments[0]
//
//                            saleRepository.updatePayment(
//                                this.payment.copy(
//                                    methodId = method.id,
//                                    methodType = method.type,
//                                    methodName = method.name,
//                                )
//                            )
//
//                            method
//                        } else {
//                            null
//                        }
//                    )
//                } else {
//                    copy(paymentMethodsAsync = it)
//                }
//            }
//    }

    private fun loadTotalPaid(saleId: String) = withState {
        suspend {
            salePaymentRepository.totalPaymentForSale(saleId)
        }.execute {
            copy(totalPaymentResponseAsync = it)
        }
    }

    private fun loadCardFlagsRepository() = withState {
        cardFlagsRepository
            .listCards()
            .execute(Dispatchers.IO) {
                copy(cardFlagsAsync = it)
            }
    }

    private fun loadFrames(serviceOrderId: String) {
        suspend {
            framesRepository.framesForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(framesResponseAsync = it)
        }
    }

    private fun loadProductPicked(serviceOrderId: String) {
        suspend{
            saleRepository.productPicked(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(productPickedResponseAsync = it)
        }
    }

    private fun processProductPickedResponse(response: ProductPickedResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    productPickedResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(productPicked = it) }
        )
    }

    private fun loadLensPicked(lensId: String) {
        suspend {
            localLensesRepository.getLensById(lensId)
        }.execute(Dispatchers.IO) {
            copy(lensResponseAsync = it)
        }
    }

    private fun processLensPickedResponse(response: SingleLensResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    lensResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(lens = it.toLens()) }
        )
    }

    private fun loadColoringPicked(coloringId: String) {
        suspend {
            localLensesRepository.getColoringById(coloringId)
        }.execute(Dispatchers.IO) {
            copy(coloringResponseAsync = it)
        }
    }

    private fun processColoringPickedResponse(response: SingleColoringResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    coloringResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(coloring = it.toColoring()) }
        )
    }

    private fun loadTreatmentPicked(treatmentId: String) {
        suspend {
            localLensesRepository.getTreatmentById(treatmentId)
        }.execute(Dispatchers.IO) {
            copy(treatmentResponseAsync = it)
        }
    }

    private fun processTreatmentPickedResponse(response: SingleTreatmentResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    treatmentResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(treatment = it.toTreatment()) }
        )
    }

    private fun processFramesResponse(response: LocalFramesRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    framesResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(frames = it.toFrames()) }
        )
    }

    private fun processDiscountResponse(response: OverallDiscountRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    discountAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(discount = it.toOverallDiscount()) }
        )
    }

    private fun processPaymentFeeResponse(response: PaymentFeeRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    paymentFeeAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(paymentFee = it.toPaymentFee()) }
        )
    }

    private fun processTotalPaymentResponse(response: SalePaymentTotalResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    totalPaymentResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(totalPaid = it) },
        )
    }

    private fun loadProducts(productsPicked: ProductPickedDocument){
        viewModelScope.launch(Dispatchers.IO) {
            loadLensPicked(productsPicked.lensId)
            loadColoringPicked(productsPicked.coloringId)
            loadTreatmentPicked(productsPicked.treatmentId)
        }
    }

    private fun processPaymentResponse(response: SinglePaymentResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    paymentResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(payment = it.toPayment()) }
        )
    }

    private fun processPaymentUpdateResponse(response: SalePaymentUpdateResult) {
        response.fold(
            ifLeft = {
                setState {
                    copy(
                        paymentUpdateResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        ),
                    )
                }
            },

            ifRight = { }
        )
    }

    private fun updateTotalToPay(
        lens: Lens,
        coloring: Coloring,
        treatment: Treatment,
        frames: Frames,
    ) = setState {
        var totalToPay = lens.price + frames.value

        if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
            totalToPay += coloring.price
        }
        if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
            totalToPay += treatment.price
        }

        if (coloring.price > 0) {
            totalToPay += lens.priceAddColoring
        }
        if (treatment.price > 0) {
            totalToPay += lens.priceAddTreatment
        }

        copy(totalToPay = totalToPay)
    }

    private fun loadDiscount(saleId: String) {
        suspend {
            discountRepository.discountForSale(saleId)
        }.execute {
            copy(discountAsync = it)
        }
    }

    private fun loadPaymentFee(saleId: String) {
        suspend {
            paymentFeeRepository.paymentFeeForSale(saleId)
        }.execute {
            copy(paymentFeeAsync = it)
        }
    }

    private fun updateTotalLeftToPay(totalToPay: Double, totalPaid: Double) = setState {
        copy(totalLeftToPay = totalToPay - totalPaid)
    }

    private fun watchPaymentDataStream(paymentId: Long) {
        salePaymentRepository
            .watchPayment(paymentId)
            .execute(Dispatchers.IO) {
                copy(paymentResponseAsync = it)
            }
    }

    private fun updatePaymentWithMethod(method: PaymentMethod)  = withState {
        suspend {
            val payment = it.payment
                .copy(
                    methodId = method.id,
                    methodType = method.type,
                    methodName = method.name,
                )
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    fun onUpdatePaymentId(paymentId: Long) = setState {
        copy(paymentId = paymentId)
    }

    fun onUpdateClientId(clientId: String) = setState {
        copy(clientId = clientId)
    }

    fun onUpdateSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onUpdateServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun loadClient(clientId: String) = withState {
        clientRepository
            .clientById(clientId)
            .execute(Dispatchers.IO) {
                Timber.i("Loading client ($clientId) $it")

                copy(clientAsync = it)
            }
    }

    fun onTotalPaidChange(value: Double) = withState {
        suspend {
            val paid = value.coerceAtMost(it.totalLeftToPay)
                .coerceAtLeast(0.0)

            val payment = it.payment
                .copy(value = paid)
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    fun onMethodPaymentChanged(document: String) = withState {
        suspend {
            val payment = it.payment
                .copy(document = document)
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    fun onCardFlagChanged(cardFlagIcon: Uri, cardFlagName: String) = withState {
        suspend {
            val payment = it.payment
                .copy(
                    cardFlagIcon = cardFlagIcon,
                    cardFlagName = cardFlagName,
                )
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    fun onIncreaseInstallments(value: Int) = withState {
        suspend {
            val maxInstallments = it.activePaymentMethod?.maxInstallments ?: 1
            val newValue = (value + 1).coerceAtMost(maxInstallments)
            val payment = it.payment
                .copy(installments = newValue)
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    fun onDecreaseInstallments(value: Int) = withState {
        suspend {
            val minInstallments = 1
            val newValue = (value - 1).coerceAtLeast(minInstallments)
            val payment = it.payment
                .copy(installments = newValue)
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    fun onPaymentMethodChanged(method: PaymentMethod)  = setState {
        copy(activePaymentMethod = method)
    }

    fun cancelPayment() = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val payment = it.payment.toSalePaymentDocument()

            val deleteResponse = salePaymentRepository.deletePayment(payment)
            if (deleteResponse.isLeft()) {
                Timber.e("Error deleting payment: $payment")
            }
        }
    }

    // hilt
    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: PaymentState): PaymentViewModel
    }

    companion object: MavericksVMFactory by hiltMavericksViewModelFactory()
}