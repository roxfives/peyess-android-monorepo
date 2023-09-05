package com.peyess.salesapp.screen.edit_service_order.payment.state

import android.net.Uri
import arrow.core.Either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.data.repository.card_flag.CardFlagRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchSingleResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchTotalResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientReadSingleResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.feature.payment.model.Coloring
import com.peyess.salesapp.feature.payment.model.Frames
import com.peyess.salesapp.feature.payment.model.Lens
import com.peyess.salesapp.feature.payment.model.OverallDiscount
import com.peyess.salesapp.feature.payment.model.PaymentFee
import com.peyess.salesapp.feature.payment.model.PaymentMethod
import com.peyess.salesapp.feature.payment.model.Treatment
import com.peyess.salesapp.repository.payments.PaymentMethodRepository
import com.peyess.salesapp.repository.payments.PaymentMethodsResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.screen.edit_service_order.payment.adapter.toFrames
import com.peyess.salesapp.screen.sale.payment.adapter.toClient
import com.peyess.salesapp.screen.sale.payment.adapter.toColoring
import com.peyess.salesapp.screen.sale.payment.adapter.toLens
import com.peyess.salesapp.screen.sale.payment.adapter.toLocalPaymentDocument
import com.peyess.salesapp.screen.sale.payment.adapter.toOverallDiscount
import com.peyess.salesapp.screen.sale.payment.adapter.toPayment
import com.peyess.salesapp.screen.sale.payment.adapter.toPaymentFee
import com.peyess.salesapp.screen.sale.payment.adapter.toPaymentMethod
import com.peyess.salesapp.screen.sale.payment.adapter.toTreatment
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPaymentViewModel, EditPaymentState>
private typealias EditPaymentViewModelFactory =
        MavericksViewModelFactory<EditPaymentViewModel, EditPaymentState>

class EditPaymentViewModel @AssistedInject constructor(
    @Assisted initialState: EditPaymentState,

    private val localPaymentRepository: EditLocalPaymentRepository,
    private val productPickedRepository: EditProductPickedRepository,
    private val discountRepository: EditPaymentDiscountRepository,
    private val paymentFeeRepository: EditPaymentFeeRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val framesRepository: EditFramesDataRepository,
    private val clientRepository: ClientRepository,
    private val cardFlagsRepository: CardFlagRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val localClientRepository: LocalClientRepository,
): MavericksViewModel<EditPaymentState>(initialState) {

    init {
        loadInitialData()

        onEach(
            EditPaymentState::clientId,
            EditPaymentState::paymentId,
        ) { clientId, paymentId ->
            loadClient(clientId)
            streamLoadPayment(paymentId, clientId)

            if (clientId.isNotBlank()) {
                updateClient(paymentId, clientId)
            }
        }

        onEach(EditPaymentState::saleId) {
            loadPaymentFee(it)
            loadDiscount(it)
            loadTotalPaid(it)
        }

        onEach(EditPaymentState::serviceOrderId) {
            loadProductPicked(it)
            loadFrames(it)
        }

        onEach(EditPaymentState::productPicked) { loadProducts(it) }

        onEach(
            EditPaymentState::lens,
            EditPaymentState::coloring,
            EditPaymentState::treatment,
            EditPaymentState::frames,
        ) { lens, coloring, treatment, frames ->
            updateTotalToPay(lens, coloring, treatment, frames)
        }

        onEach(
            EditPaymentState::totalToPay,
            EditPaymentState::totalPaid,
            EditPaymentState::discount,
            EditPaymentState::paymentFee,
        ) { totalToPay, totalPaid, discount, paymentFee ->
            updateTotalLeftToPay(totalToPay, totalPaid, discount, paymentFee)
        }

        onAsync(EditPaymentState::clientResponseAsync) { processClientResponse(it) }

        onAsync(EditPaymentState::productPickedResponseAsync) { processProductPickedResponse(it) }
        onAsync(EditPaymentState::lensResponseAsync) { processLensPickedResponse(it) }
        onAsync(EditPaymentState::coloringResponseAsync) { processColoringPickedResponse(it) }
        onAsync(EditPaymentState::treatmentResponseAsync) { processTreatmentPickedResponse(it) }
        onAsync(EditPaymentState::framesResponseAsync) { processFramesResponse(it) }

        onAsync(EditPaymentState::discountAsync) { processDiscountResponse(it) }
        onAsync(EditPaymentState::paymentFeeAsync) { processPaymentFeeResponse(it) }

        onAsync(EditPaymentState::paymentResponseAsync) { processPaymentResponse(it) }

        onAsync(EditPaymentState::paymentMethodsResponseAsync) { processPaymentMethodsResponse(it) }
        onAsync(EditPaymentState::totalPaymentResponseAsync) { processTotalPaymentResponse(it) }

        onAsync(EditPaymentState::cardFlagsAsync) { processCardFlagsResponse(it) }
    }

    private fun loadInitialData() {
        loadPaymentMethods()
        loadCardFlags()
    }

    private fun streamLoadPayment(paymentId: Long, clientId: String) {
        localPaymentRepository
            .streamPaymentById(paymentId, clientId)
            .execute(Dispatchers.IO) {
                copy(paymentResponseAsync = it)
            }
    }

    private fun processPaymentResponse(
        response: EditLocalPaymentFetchSingleResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(paymentsResponseAsync = Fail(it.error)) },

            ifRight = { copy(paymentInput = it.toPayment()) },
        )
    }

    private fun loadClient(clientId: String) {
        suspend {
            localClientRepository.clientById(clientId)
        }.execute(Dispatchers.IO) {
            copy(clientResponseAsync = it)
        }
    }

    private fun processClientResponse(response: LocalClientReadSingleResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    paymentsResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(client = it.toClient()) },
        )
    }

    private fun updateClient(paymentId: Long, clientId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateClientId(paymentId, clientId)
        }
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
                    paymentMethodsResponseAsync = Fail(it.error ?: Throwable(it.description))
                )
            },

            ifRight = { copy(paymentMethods = it.map { method -> method.toPaymentMethod() }) },
        )
    }

    private fun loadCardFlags() = withState {
        cardFlagsRepository
            .listCards()
            .execute(Dispatchers.IO) {
                copy(cardFlagsAsync = it)
            }
    }

    private fun processCardFlagsResponse(response: List<CardFlagDocument>) = setState {
        copy(cardFlags = response)
    }

    private fun loadProductPicked(serviceOrderId: String) {
        suspend{
            productPickedRepository.productPickedForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(productPickedResponseAsync = it)
        }
    }

    private fun processProductPickedResponse(
        response: EditProductPickedFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(productPickedResponseAsync = Fail(it.error)) },

            ifRight = { copy(productPicked = it) },
        )
    }

    private fun loadProducts(productsPicked: ProductPickedDocument) {
        loadLensPicked(productsPicked.lensId)
        loadColoringPicked(productsPicked.lensId, productsPicked.coloringId)
        loadTreatmentPicked(productsPicked.lensId, productsPicked.treatmentId)
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

            ifRight = { copy(lens = it.toLens()) },
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

    private fun loadFrames(serviceOrderId: String) {
        suspend {
            framesRepository.findFramesForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(framesResponseAsync = it)
        }
    }

    private fun processFramesResponse(response: EditFramesFetchResponse) = setState {
        response.fold(
            ifLeft = { copy(framesResponseAsync = Fail(it.error)) },

            ifRight = { copy(frames = it.toFrames()) },
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

        if (coloring.price > BigDecimal.ZERO) {
            totalToPay += lens.priceAddColoring
        }
        if (treatment.price > BigDecimal.ZERO) {
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

    private fun processDiscountResponse(response: EditPaymentDiscountFetchResponse) = setState {
        response.fold(
            ifLeft = { copy(discountAsync = Fail(it.error)) },

            ifRight = { copy(discount = it.toOverallDiscount()) },
        )
    }

    private fun loadPaymentFee(saleId: String) {
        suspend {
            paymentFeeRepository.paymentFeeForSale(saleId)
        }.execute(Dispatchers.IO) {
            copy(paymentFeeAsync = it)
        }
    }

    private fun processPaymentFeeResponse(response: EditPaymentFeeFetchResponse) = setState {
        response.fold(
            ifLeft = { copy(paymentFeeAsync = Fail(it.error)) },

            ifRight = { copy(paymentFee = it.toPaymentFee()) },
        )
    }

    private fun loadTotalPaid(saleId: String) = withState {
        localPaymentRepository.streamTotalPaid(saleId)
            .execute(Dispatchers.IO) {
                copy(totalPaymentResponseAsync = it)
            }
    }

    private fun processTotalPaymentResponse(
        response: EditLocalPaymentFetchTotalResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(totalPaymentResponseAsync = Fail(it.error)) },

            ifRight = { copy(totalPaid = it) },
        )
    }

    private fun updateTotalLeftToPay(
        totalToPay: BigDecimal,
        totalPaid: BigDecimal,
        discount: OverallDiscount,
        paymentFee: PaymentFee,
    ) = setState {
        val totalWithDiscount = calculateTotalWithDiscount(totalToPay, discount)
        val total = calculatePaymentWithFee(totalWithDiscount, paymentFee)

        copy(totalLeftToPay = total - totalPaid)
    }

    private fun calculateTotalWithDiscount(
        totalToPay: BigDecimal,
        discount: OverallDiscount,
    ): BigDecimal {
        return when (discount.discountMethod) {
            DiscountCalcMethod.None -> totalToPay
            DiscountCalcMethod.Percentage -> totalToPay * (BigDecimal.ONE - discount.overallDiscountValue)
            DiscountCalcMethod.Whole -> totalToPay - discount.overallDiscountValue
        }
    }

    private fun calculatePaymentWithFee(
        totalToPay: BigDecimal,
        paymentFee: PaymentFee,
    ): BigDecimal {
        return when (paymentFee.method) {
            PaymentFeeCalcMethod.None -> totalToPay
            PaymentFeeCalcMethod.Percentage -> totalToPay * (BigDecimal.ONE + paymentFee.value)
            PaymentFeeCalcMethod.Whole -> totalToPay + paymentFee.value
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

    fun onTotalPaidChange(value: BigDecimal) = setState {
        val maxPayment = paymentInput.value + totalLeftToPay
        val paid = value.coerceAtMost(maxPayment)
            .coerceAtLeast(BigDecimal.ZERO)
            .setScale(2, RoundingMode.HALF_EVEN)
        val update = paymentInput.copy(value = paid)

        viewModelScope.launch(Dispatchers.IO) { localPaymentRepository.updateValue(paymentId, paid) }
        copy(paymentInput = update)
    }

    fun onDocumentChanged(document: String) = setState {
        val update = paymentInput.copy(document = document)

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateDocument(paymentId, document)
        }
        copy(paymentInput = update)
    }

    fun onCardFlagChanged(cardFlagIcon: Uri, cardFlagName: String) = setState {
        val update = paymentInput.copy(
            cardFlagIcon = cardFlagIcon,
            cardFlagName = cardFlagName,
        )

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateCardFlagIcon(paymentId, cardFlagIcon)
            localPaymentRepository.updateCardFlagName(paymentId, cardFlagName)
        }
        copy(paymentInput = update)
    }

    fun onIncreaseInstallments(value: Int) = setState {
        val maxInstallments = activePaymentMethod.maxInstallments
        val newValue = (value + 1).coerceAtMost(maxInstallments)
        val update = paymentInput.copy(installments = newValue)

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateInstallments(paymentId, newValue)
        }
        copy(paymentInput = update)
    }

    fun onDecreaseInstallments(value: Int) = setState {
        val minInstallments = 1
        val newValue = (value - 1).coerceAtLeast(minInstallments)
        val payment = paymentInput.copy(
            dueDatePeriod = newValue,
            dueDate = paymentInput.dueDateMode.dueDateAfter(newValue),
        )

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateInstallments(paymentId, newValue)
        }
        copy(paymentInput = payment)
    }

    fun onDueDateChanged(date: ZonedDateTime) = setState {
        val totalDays = ChronoUnit.DAYS.between(date, ZonedDateTime.now()).toInt()
        val payment = paymentInput.copy(
            dueDateMode = PaymentDueDateMode.Day,
            dueDatePeriod = totalDays,
            dueDate = date,
        )

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateDueDateData(
                paymentId = paymentId,
                dueDateMode = payment.dueDateMode,
                dueDatePeriod = payment.dueDatePeriod,
                dueDate = payment.dueDate,
            )
        }
        copy(paymentInput = payment)
    }

    fun onLegalIdChanged(legalId: String) = setState {
        val payment = paymentInput.copy(
           legalId = legalId,
        )

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateLegalId(
                paymentId = paymentId,
                legalId = legalId,
            )
        }
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

        viewModelScope.launch(Dispatchers.IO) {
            localPaymentRepository.updateMethodId(paymentId, update.methodId)
            localPaymentRepository.updateMethodType(paymentId, update.methodType)
            localPaymentRepository.updateMethodName(paymentId, update.methodName)
            localPaymentRepository.updateInstallments(paymentId, update.installments)
            localPaymentRepository.updateDueDateData(
                paymentId = paymentId,
                dueDateMode = update.dueDateMode,
                dueDatePeriod = update.dueDatePeriod,
                dueDate = update.dueDateMode.dueDateAfter(update.dueDatePeriod),
            )
            localPaymentRepository.updateHasLegalId(paymentId, method.hasLegalId)
        }
        copy(paymentInput = update)
    }

    fun checkPaymentForMissingClient() = withState {
        if (it.paymentInput.clientId.isBlank()) {
            // TODO: Add warning through a snackBar
            cancelPayment()
        }
    }

    fun cancelPayment() = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val payment = it.paymentInput.toLocalPaymentDocument()

            val deleteResponse = localPaymentRepository.deletePayment(payment.id)
            if (deleteResponse.isLeft()) {
                Timber.e("Error deleting payment: $payment")
            }
        }
    }

    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: EditPaymentState): EditPaymentViewModel
    }

    companion object: EditPaymentViewModelFactory by hiltMavericksViewModelFactory()
}