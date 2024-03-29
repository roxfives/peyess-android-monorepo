package com.peyess.salesapp.screen.edit_service_order.service_order.state

import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningFetchBothResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_sale.positioning.typing.PositioningPair
import com.peyess.salesapp.feature.service_order.model.Client
import com.peyess.salesapp.feature.service_order.model.Coloring
import com.peyess.salesapp.feature.service_order.model.Frames
import com.peyess.salesapp.feature.service_order.model.Lens
import com.peyess.salesapp.feature.service_order.model.Payment
import com.peyess.salesapp.feature.service_order.model.Prescription
import com.peyess.salesapp.feature.service_order.model.Treatment
import com.peyess.salesapp.features.edit_service_order.fetcher.ServiceOrderFetchResponse
import com.peyess.salesapp.features.edit_service_order.updater.UpdateSaleResponse
import com.peyess.salesapp.features.edit_service_order.updater.error.GenerateSaleDataError
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toMeasuring
import com.peyess.salesapp.screen.edit_service_order.service_order.model.ServiceOrder
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.typing.sale.PurchaseState
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

data class EditServiceOrderState(
    val serviceOrderId: String = "",
    val saleId: String = "",
    val shouldFetchFromServer: Boolean = false,

    val serviceOrderFetchResponseAsync: Async<ServiceOrderFetchResponse> = Uninitialized,
    val currentPurchase: PurchaseDocument = PurchaseDocument(),
    val currentServiceOrder: ServiceOrderDocument = ServiceOrderDocument(),

    val sellerResponseAsync: Async<CollaboratorDocument?> = Uninitialized,
    val seller: CollaboratorDocument = CollaboratorDocument(),

    val serviceOrderResponseAsync: Async<EditServiceOrderFetchResponse> = Uninitialized,
    val serviceOrder: ServiceOrder = ServiceOrder(),

    val userPickedResponseAsync: Async<EditClientPickedFetchResponse> = Uninitialized,
    val userPicked: Client = Client(),

    val responsiblePickedResponseAsync: Async<EditClientPickedFetchResponse> = Uninitialized,
    val responsiblePicked: Client = Client(),

    val witnessPickedResponseAsync: Async<EditClientPickedFetchResponse> = Uninitialized,
    val witnessPicked: Client = Client(),
    val hasWitness: Boolean = false,

    val prescriptionResponseAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
    val prescription: Prescription = Prescription(),

    val positioningsResponseAsync: Async<EditPositioningFetchBothResponse> = Uninitialized,
    val positioningPair: PositioningPair = PositioningPair(),

    val productPickedResponseAsync: Async<EditProductPickedFetchResponse> = Uninitialized,
    val productPicked: ProductPickedDocument = ProductPickedDocument(),

    val lensResponseAsync: Async<SingleLensResponse> = Uninitialized,
    val lens: Lens = Lens(),

    val coloringResponseAsync: Async<SingleColoringResponse> = Uninitialized,
    val coloringResponse: Coloring = Coloring(),

    val treatmentResponseAsync: Async<SingleTreatmentResponse> = Uninitialized,
    val treatmentResponse: Treatment = Treatment(),

    val framesResponseAsync: Async<EditFramesFetchResponse> = Uninitialized,
    val frames: Frames = Frames(),

    val paymentsResponseAsync: Async<EditLocalPaymentFetchResponse> = Uninitialized,
    val payments: List<Payment> = emptyList(),

    val discountResponseAsync: Async<EditPaymentDiscountFetchResponse> = Uninitialized,
    val discount: OverallDiscountDocument = OverallDiscountDocument(),

    val feeResponseAsync: Async<EditPaymentFeeFetchResponse> = Uninitialized,
    val fee: PaymentFeeDocument = PaymentFeeDocument(),

    val pdfGenerationAsync: Async<Either<GenerateSaleDataError, Unit>> = Uninitialized,

    val saleGenerationAsync: Async<UpdateSaleResponse> = Uninitialized,

    val hasSaleUpdateFailed: Boolean = false,
): MavericksState {
    val totalPaid = payments.map { p -> p.value }
        .ifEmpty { listOf(BigDecimal.ZERO) }
        .reduce(BigDecimal::add)

    val successfullyFetchedServiceOrder = serviceOrderFetchResponseAsync is Success
            && serviceOrderFetchResponseAsync.invoke().isRight()
    val errorWhileFetchingServiceOrder = serviceOrderFetchResponseAsync is Fail ||
            (serviceOrderFetchResponseAsync is Success
                    && serviceOrderFetchResponseAsync.invoke().isLeft())

    val isLoadingSaleData = serviceOrderFetchResponseAsync is Loading
            || sellerResponseAsync is Loading
            || serviceOrderResponseAsync is Loading
            || userPickedResponseAsync is Loading
            || responsiblePickedResponseAsync is Loading
            || witnessPickedResponseAsync is Loading
            || prescriptionResponseAsync is Loading
            || positioningsResponseAsync is Loading
            || productPickedResponseAsync is Loading
            || lensResponseAsync is Loading
            || coloringResponseAsync is Loading
            || treatmentResponseAsync is Loading
            || framesResponseAsync is Loading
            || paymentsResponseAsync is Loading
            || discountResponseAsync is Loading
            || feeResponseAsync is Loading
            || serviceOrderFetchResponseAsync is Uninitialized
            || sellerResponseAsync is Uninitialized
            || serviceOrderResponseAsync is Uninitialized
            || userPickedResponseAsync is Uninitialized
            || responsiblePickedResponseAsync is Uninitialized
            || witnessPickedResponseAsync is Uninitialized
            || prescriptionResponseAsync is Uninitialized
            || positioningsResponseAsync is Uninitialized
            || productPickedResponseAsync is Uninitialized
            || lensResponseAsync is Uninitialized
            || coloringResponseAsync is Uninitialized
            || treatmentResponseAsync is Uninitialized
            || framesResponseAsync is Uninitialized
            || paymentsResponseAsync is Uninitialized
            || discountResponseAsync is Uninitialized
            || feeResponseAsync is Uninitialized

    val canUpdate = currentPurchase.state == PurchaseState.PendingConfirmation

    val isUpdatingSaleData = saleGenerationAsync is Loading
    val hasSaleDataUpdateFailed = saleGenerationAsync is Fail
            || (saleGenerationAsync is Success && saleGenerationAsync.invoke().isLeft())
    val isSaleDone = saleGenerationAsync is Success && saleGenerationAsync.invoke().isRight()

    val isGeneratingPdf = pdfGenerationAsync is Loading
    val hasPdfGenerationFailed = pdfGenerationAsync is Fail
            || (pdfGenerationAsync is Success && pdfGenerationAsync.invoke().isLeft())

    val measuringLeft = positioningPair.left.toMeasuring()
    val measuringRight = positioningPair.right.toMeasuring()

    val coloring = Pair(lens, coloringResponse).let {
        if (lens.isColoringDiscounted || lens.isColoringIncluded) {
            coloringResponse.copy(price = BigDecimal.ZERO)
        } else {
            coloringResponse
        }
    }

    val treatment = Pair(lens, treatmentResponse).let {
        if (lens.isTreatmentDiscounted || lens.isTreatmentIncluded) {
            treatmentResponse.copy(price = BigDecimal.ZERO)
        } else {
            treatmentResponse
        }
    }

    val fullPrice = lens.price +
            coloring.price +
            treatment.price +
            frames.let {
                if (it.areFramesNew) {
                    it.value
                } else {
                    BigDecimal.ZERO
                }
            }
    val finalPrice = calculateFinalPrice(fullPrice, discount, fee)
    val priceWithDiscountOnly = calculatePriceWithDiscount(fullPrice, discount)
    val canAddNewPayment = totalPaid < finalPrice

    val confirmationMessage = if (finalPrice <= totalPaid) {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumIntegerDigits = 1

        "Deseja finalizar a compra no valor de ${currencyFormatter.format(finalPrice)}"
    } else {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumIntegerDigits = 1

        val missing = (finalPrice - totalPaid).abs()
        "Deseja finalizar a compra no valor de ${currencyFormatter.format(finalPrice)}" +
                " com o saldo à receber de ${currencyFormatter.format(missing)}"
    }

    private fun calculatePriceWithDiscount(
        fullPrice: BigDecimal,
        discount: OverallDiscountDocument,
    ): BigDecimal {
        val discountAsWhole = when (discount.discountMethod) {
            DiscountCalcMethod.None -> BigDecimal.ZERO
            DiscountCalcMethod.Percentage -> discount.overallDiscountValue * fullPrice
            DiscountCalcMethod.Whole -> discount.overallDiscountValue
        }

        return fullPrice - discountAsWhole
    }

    private fun calculateFinalPrice(
        fullPrice: BigDecimal,
        discount: OverallDiscountDocument,
        fee: PaymentFeeDocument
    ): BigDecimal {
        val discountAsWhole = when (discount.discountMethod) {
            DiscountCalcMethod.None -> BigDecimal.ZERO
            DiscountCalcMethod.Percentage -> fullPrice * discount.overallDiscountValue
            DiscountCalcMethod.Whole -> discount.overallDiscountValue
        }

        val feeAsWhole = when (fee.method) {
            PaymentFeeCalcMethod.None -> BigDecimal.ZERO
            PaymentFeeCalcMethod.Percentage -> discountAsWhole * fee.value
            PaymentFeeCalcMethod.Whole -> fee.value
        }

        return fullPrice - discountAsWhole + feeAsWhole
    }
}
