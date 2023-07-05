package com.peyess.salesapp.screen.sale.payment.state

import android.net.Uri
import arrow.core.Either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientReadSingleResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentRepository
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentTotalResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentUpdateResult
import com.peyess.salesapp.data.repository.local_sale.payment.SinglePaymentResponse
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.screen.sale.payment.adapter.toClient
import com.peyess.salesapp.screen.sale.payment.adapter.toColoring
import com.peyess.salesapp.screen.sale.payment.adapter.toFrames
import com.peyess.salesapp.screen.sale.payment.adapter.toLens
import com.peyess.salesapp.screen.sale.payment.adapter.toOverallDiscount
import com.peyess.salesapp.screen.sale.payment.adapter.toPayment
import com.peyess.salesapp.screen.sale.payment.adapter.toPaymentFee
import com.peyess.salesapp.screen.sale.payment.adapter.toPaymentMethod
import com.peyess.salesapp.screen.sale.payment.adapter.toLocalPaymentDocument
import com.peyess.salesapp.screen.sale.payment.adapter.toTreatment
import com.peyess.salesapp.feature.payment.model.Client
import com.peyess.salesapp.feature.payment.model.Coloring
import com.peyess.salesapp.feature.payment.model.Frames
import com.peyess.salesapp.feature.payment.model.Lens
import com.peyess.salesapp.feature.payment.model.OverallDiscount
import com.peyess.salesapp.feature.payment.model.Payment
import com.peyess.salesapp.feature.payment.model.PaymentFee
import com.peyess.salesapp.feature.payment.model.PaymentMethod
import com.peyess.salesapp.feature.payment.model.Treatment
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.payments.PaymentMethodsResponse
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
    private val localClientRepository: LocalClientRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val cardFlagsRepository: CardFlagRepository,
    private val discountRepository: OverallDiscountRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
    private val clientRepository: ClientRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val framesRepository: LocalFramesRepository,
    private val localPaymentRepository: LocalPaymentRepository,
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
        onAsync(PaymentState::finishPaymentResponseAsync) { processFinishPaymentResponse(it) }

        onAsync(PaymentState::paymentMethodsResponseAsync) { processPaymentMethodsResponse(it) }

        onAsync(PaymentState::clientResponseAsync) { processClientResponse(it) }

        onEach(PaymentState::paymentInput) { updatePayment(it) }

        onEach(
            PaymentState::paymentInput,
            PaymentState::client,
        ) { _, client -> updatePaymentWithClient(client) }
        onEach(
            PaymentState::paymentInput,
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

    private fun updatePayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updatePayment(payment.toLocalPaymentDocument())
        }
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
        localPaymentRepository.watchTotalPayment(saleId)
            .execute(Dispatchers.IO) {
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

    private fun loadColoringPicked(lensId: String, coloringId: String) {
        suspend {
            localLensesRepository.getColoringById(lensId, coloringId)
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

    private fun loadTreatmentPicked(lensId: String, treatmentId: String) {
        suspend {
            localLensesRepository.getTreatmentById(lensId, treatmentId)
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

    private fun processTotalPaymentResponse(response: LocalPaymentTotalResponse) = setState {
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
        loadLensPicked(productsPicked.lensId)
        loadColoringPicked(productsPicked.lensId, productsPicked.coloringId)
        loadTreatmentPicked(productsPicked.lensId, productsPicked.treatmentId)
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

            ifRight = { copy(paymentInput = it.toPayment()) }
        )
    }

    private fun processFinishPaymentResponse(response: LocalPaymentUpdateResult) = setState {
        response.fold(
            ifLeft = {
                copy(
                    finishPaymentResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = {
                checkPaymentForMissingClient()
                copy(finishedPayment = true)
            }
        )
    }

    private fun checkPaymentForMissingClient() = withState {
        if (it.paymentInput.clientId.isBlank()) {
            // TODO: Add warning through a snackBar
            cancelPayment()
        }
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

    private fun loadClient(clientId: String) {
        suspend {
            localClientRepository.clientById(clientId)
        }.execute(Dispatchers.IO) {
            copy(clientResponseAsync = it)
        }
    }

    private fun watchPaymentDataStream(paymentId: Long) {
        localPaymentRepository
            .watchPayment(paymentId)
            .execute(Dispatchers.IO) {
                copy(paymentResponseAsync = it)
            }
    }

    private fun processClientResponse(response: LocalClientReadSingleResponse) = setState {
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

    private fun updatePaymentWithClient(client: Client)  = setState {
        val update = paymentInput.copy(
            clientId = client.id,
            clientName = client.name,
            clientDocument = client.document,
            clientAddress = client.shortAddress,
        )

        copy(paymentInput = update)
    }

    private fun updatePaymentWithMethodList(methods: List<PaymentMethod>) = withState { state ->
        if (state.paymentInput.methodId.isBlank() && methods.isNotEmpty()) {
            val defaultMethod = methods.first()

            val methodId = defaultMethod.id
            val methodType = defaultMethod.type
            val methodName = defaultMethod.name

            val payment = state.paymentInput.copy(
                methodId = methodId,
                methodType = methodType,
                methodName = methodName,

                dueDatePeriod = defaultMethod.dueDateDefault,
                dueDateMode = defaultMethod.dueDateMode,
                dueDate = defaultMethod.dueDateMode.dueDateAfter(defaultMethod.dueDateDefault),
            )

            setState {
                copy(paymentInput = payment)
            }
        }
    }

    suspend fun pictureForClient(clientId: String): Uri {
        return Either.catch {
            clientRepository.pictureForClient(clientId)
        }.mapLeft {
            Timber.e("Error getting picture for client $clientId: ${it.message}", it)
        }.fold(
            ifLeft = { Uri.EMPTY },
            ifRight = { it },
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

    fun onTotalPaidChange(value: Double) = setState {
        val paid = value.coerceAtMost(paymentInput.value + totalLeftToPay)
            .coerceAtLeast(0.0)
        val update = paymentInput.copy(value = paid)

        copy(paymentInput = update)
    }

    fun onMethodPaymentChanged(document: String) = setState {
        val update = paymentInput.copy(document = document)

        copy(paymentInput = update)
    }

    fun onCardFlagChanged(cardFlagIcon: Uri, cardFlagName: String) = setState {
        val update = paymentInput.copy(
            cardFlagIcon = cardFlagIcon,
            cardFlagName = cardFlagName,
        )

        copy(paymentInput = update)
    }

    fun onIncreaseInstallments(value: Int) = setState {
        val maxInstallments = activePaymentMethod.maxInstallments
        val newValue = (value + 1).coerceAtMost(maxInstallments)
        val update = paymentInput.copy(installments = newValue)

        copy(paymentInput = update)
    }

    fun onDecreaseInstallments(value: Int) = setState {
        val minInstallments = 1
        val newValue = (value - 1).coerceAtLeast(minInstallments)
        val payment = paymentInput.copy(installments = newValue)

        copy(paymentInput = payment)
    }

    fun onIncreasePeriodDueDate(value: Int) = setState {
        val maxPeriod = this.activePaymentMethod.dueDateMax
        val newValue = (value + 1).coerceAtMost(maxPeriod)
        val payment = paymentInput.copy(
            dueDatePeriod = newValue,
            dueDate = paymentInput.dueDateMode.dueDateAfter(newValue),
        )

        copy(paymentInput = payment)
    }

    fun onDecreasePeriodDueDate(value: Int) = setState {
        val minPeriod = this.activePaymentMethod.dueDateDefault
        val newValue = (value - 1).coerceAtLeast(minPeriod)
        val payment = paymentInput.copy(
            dueDatePeriod = newValue,
            dueDate = paymentInput.dueDateMode.dueDateAfter(newValue),
        )

        copy(paymentInput = payment)
    }

    fun onPaymentMethodChanged(method: PaymentMethod) = setState {
        val maxInstallments = paymentInput.installments
            .coerceAtMost(method.maxInstallments)

        val update = paymentInput.copy(
            methodId = method.id,
            methodType = method.type,
            methodName = method.name,
            installments = maxInstallments,

            dueDateMode = method.dueDateMode,
            dueDatePeriod = method.dueDateDefault,
            dueDate = method.dueDateMode.dueDateAfter(period = method.dueDateDefault),
        )

        copy(paymentInput = update)
    }

    fun onFinishPayment() = withState {
        suspend {
            val payment = it.paymentInput.toLocalPaymentDocument()

            localPaymentRepository.updatePayment(payment)
        }.execute(Dispatchers.IO) {
            copy(finishPaymentResponseAsync = it)
        }
    }

    fun cancelPayment() = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val payment = it.paymentInput.toLocalPaymentDocument()

            val deleteResponse = localPaymentRepository.deletePayment(payment)
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