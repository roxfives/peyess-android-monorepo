package com.peyess.salesapp.feature.sale.payment.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.client.ClientRepositoryResponse
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
import com.peyess.salesapp.feature.sale.discount.model.Discount
import com.peyess.salesapp.feature.sale.payment.adapter.toClient
import com.peyess.salesapp.feature.sale.payment.adapter.toColoring
import com.peyess.salesapp.feature.sale.payment.adapter.toFrames
import com.peyess.salesapp.feature.sale.payment.adapter.toLens
import com.peyess.salesapp.feature.sale.payment.adapter.toOverallDiscount
import com.peyess.salesapp.feature.sale.payment.adapter.toPayment
import com.peyess.salesapp.feature.sale.payment.adapter.toPaymentFee
import com.peyess.salesapp.feature.sale.payment.adapter.toPaymentMethod
import com.peyess.salesapp.feature.sale.payment.adapter.toSalePaymentDocument
import com.peyess.salesapp.feature.sale.payment.adapter.toTreatment
import com.peyess.salesapp.feature.sale.payment.model.Client
import com.peyess.salesapp.feature.sale.payment.model.Coloring
import com.peyess.salesapp.feature.sale.payment.model.Frames
import com.peyess.salesapp.feature.sale.payment.model.Lens
import com.peyess.salesapp.feature.sale.payment.model.OverallDiscount
import com.peyess.salesapp.feature.sale.payment.model.PaymentFee
import com.peyess.salesapp.feature.sale.payment.model.PaymentMethod
import com.peyess.salesapp.feature.sale.payment.model.Treatment
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.payments.PaymentMethodsResponse
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
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

        onAsync(PaymentState::paymentMethodsResponseAsync) { processPaymentMethodsResponse(it) }

        onAsync(PaymentState::clientResponseAsync) { processClientResponse(it) }

        onEach(
            PaymentState::payment,
            PaymentState::client,
        ) { _, client -> updatePaymentWithClient(client) }
        onEach(
            PaymentState::payment,
            PaymentState::paymentMethods,
        ) { _, methods -> updatePaymentWithMethodList(methods) }


        onEach(
            PaymentState::totalToPay,
            PaymentState::totalPaid,
            PaymentState::discount,
            PaymentState::paymentFee,
        ) { totalToPay, totalPaid, discount, paymentFee ->
            updateTotalLeftToPay(totalToPay, totalPaid, discount, paymentFee)
        }

        onEach(PaymentState::paymentId) { watchPaymentDataStream(it) }
        onEach(PaymentState::clientId) { loadClient(it) }

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

        loadInitialData()
    }

    private fun loadInitialData() {
        loadPaymentMethods()
        loadCardFlagsRepository()
    }

    private fun loadPaymentMethods() {
        suspend {
            paymentMethodRepository.payments()
        }.execute(Dispatchers.IO) {
            copy(paymentMethodsResponseAsync = it)
        }
    }

    private fun processPaymentMethodsResponse(response: PaymentMethodsResponse) = setState {
        response.fold(
            ifLeft = {
                 copy(
                     paymentMethodsResponseAsync = Fail(
                         it.error ?: Throwable(it.description),
                     ),
                 )
            },

            ifRight = { copy(paymentMethods = it.map { m -> m.toPaymentMethod() }) }
        )
    }

    private fun loadTotalPaid(saleId: String) = withState {
        suspend {
            salePaymentRepository.totalPaymentForSale(saleId)
        }.execute(Dispatchers.IO) {
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
        }.execute(Dispatchers.IO) {
            copy(discountAsync = it)
        }
    }

    private fun loadPaymentFee(saleId: String) {
        suspend {
            paymentFeeRepository.paymentFeeForSale(saleId)
        }.execute(Dispatchers.IO) {
            copy(paymentFeeAsync = it)
        }
    }

    private fun calculateTotalWithDiscount(totalToPay: Double, discount: OverallDiscount): Double {
        return when (discount.discountMethod) {
            DiscountCalcMethod.None -> totalToPay
            DiscountCalcMethod.Percentage -> totalToPay * (1.0 - discount.overallDiscountValue)
            DiscountCalcMethod.Whole -> totalToPay - discount.overallDiscountValue
        }
    }

    private fun calculatePaymentWithFee(totalToPay: Double, paymentFee: PaymentFee): Double {
        return when (paymentFee.method) {
            PaymentFeeCalcMethod.None -> totalToPay
            PaymentFeeCalcMethod.Percentage -> totalToPay * (1.0 + paymentFee.value)
            PaymentFeeCalcMethod.Whole -> totalToPay + paymentFee.value
        }
    }

    private fun updateTotalLeftToPay(
        totalToPay: Double,
        totalPaid: Double,
        discount: OverallDiscount,
        paymentFee: PaymentFee,
    ) = setState {
        val totalWithDiscount = calculateTotalWithDiscount(totalToPay, discount)
        val total = calculatePaymentWithFee(totalWithDiscount, paymentFee)

        copy(totalLeftToPay = total - totalPaid)
    }

    private fun watchPaymentDataStream(paymentId: Long) {
        salePaymentRepository
            .watchPayment(paymentId)
            .execute(Dispatchers.IO) {
                copy(paymentResponseAsync = it)
            }
    }

    private fun updatePaymentWithClient(client: Client)  = withState {
        suspend {
            val payment = it.payment
                .copy(
                    clientId = client.id,
                    clientName = client.name,
                    clientDocument = client.document,
                    clientAddress = client.shortAddress,
                    clientPicture = client.picture,
                ).toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
    }

    private fun updatePaymentWithMethodList(methods: List<PaymentMethod>) = withState { state ->
        if (state.payment.methodId.isBlank() && methods.isNotEmpty()) {
            suspend {
                val defaultMethod = methods.first()

                val methodId = defaultMethod.id
                val methodType = defaultMethod.type
                val methodName = defaultMethod.name

                val payment = state.payment.copy(
                    methodId = methodId,
                    methodType = methodType,
                    methodName = methodName,
                ).toSalePaymentDocument()

                salePaymentRepository.updatePayment(payment)
            }.execute(Dispatchers.IO) {
                copy(paymentUpdateResponseAsync = it)
            }
        }
    }

    private fun loadClient(clientId: String) {
        suspend {
            clientRepository.clientById(clientId)
        }.execute(Dispatchers.IO) {
            copy(clientResponseAsync = it)
        }
    }

    private fun processClientResponse(response: ClientRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    paymentResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(client = it.toClient()) }
        )
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
            val maxInstallments = it.activePaymentMethod.maxInstallments
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

    fun onPaymentMethodChanged(method: PaymentMethod) = withState {
        suspend {
            val maxInstallments = it.payment
                .installments
                .coerceAtMost(method.maxInstallments)

            val payment = it.payment
                .copy(
                    methodId = method.id,
                    methodType = method.type,
                    methodName = method.name,
                    installments = maxInstallments,
                )
                .toSalePaymentDocument()

            salePaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(paymentUpdateResponseAsync = it)
        }
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