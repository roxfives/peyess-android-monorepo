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
import com.peyess.salesapp.features.edit_service_order.updater.SaleDataResponse
import com.peyess.salesapp.features.edit_service_order.updater.UpdateServiceOrderResponse
import com.peyess.salesapp.features.edit_service_order.updater.error.GenerateSaleDataError
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toMeasuring
import com.peyess.salesapp.screen.edit_service_order.service_order.model.ServiceOrder
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

data class EditServiceOrderState(
    val serviceOrderId: String = "",
    val saleId: String = "",

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
    val totalPaid: Double = 0.0,

    val discountResponseAsync: Async<EditPaymentDiscountFetchResponse> = Uninitialized,
    val discount: OverallDiscountDocument = OverallDiscountDocument(),

    val feeResponseAsync: Async<EditPaymentFeeFetchResponse> = Uninitialized,
    val fee: PaymentFeeDocument = PaymentFeeDocument(),

    val pdfGenerationAsync: Async<Either<GenerateSaleDataError, Unit>> = Uninitialized,

    val saleGenerationAsync: Async<UpdateServiceOrderResponse> = Uninitialized,
): MavericksState {
    val isGeneratingPdf = pdfGenerationAsync is Loading
    val hasPdfGenerationFailed = pdfGenerationAsync is Fail
            || (pdfGenerationAsync is Success && pdfGenerationAsync.invoke().isLeft())

    val measuringLeft = positioningPair.left.toMeasuring()
    val measuringRight = positioningPair.right.toMeasuring()

    val coloring = Pair(lens, coloringResponse).let {
        if (lens.isColoringDiscounted || lens.isColoringIncluded) {
            coloringResponse.copy(price = 0.0)
        } else {
            coloringResponse
        }
    }

    val treatment = Pair(lens, treatmentResponse).let {
        if (lens.isTreatmentDiscounted || lens.isTreatmentIncluded) {
            treatmentResponse.copy(price = 0.0)
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
                    0.0
                }
            }
    val finalPrice = calculateFinalPrice(fullPrice, discount, fee)
    val priceWithDiscountOnly = calculatePriceWithDiscount(fullPrice, discount)
    val canAddNewPayment = totalPaid < finalPrice


    val successfullyFetchedServiceOrder = serviceOrderFetchResponseAsync is Success
            && serviceOrderFetchResponseAsync.invoke().isRight()
    val errorWhileFetchingServiceOrder = serviceOrderFetchResponseAsync is Fail ||
            (serviceOrderFetchResponseAsync is Success
                    && serviceOrderFetchResponseAsync.invoke().isLeft())

    private fun calculatePriceWithDiscount(
        fullPrice: Double,
        discount: OverallDiscountDocument,
    ): Double {
        val discountAsPercentage = when (discount.discountMethod) {
            DiscountCalcMethod.None -> 0.0
            DiscountCalcMethod.Percentage -> discount.overallDiscountValue
            DiscountCalcMethod.Whole -> discount.overallDiscountValue / fullPrice
        }

        return fullPrice * (1 - discountAsPercentage)
    }

    private fun calculateFinalPrice(
        fullPrice: Double,
        discount: OverallDiscountDocument,
        fee: PaymentFeeDocument
    ): Double {
        val discountAsPercentage = when (discount.discountMethod) {
            DiscountCalcMethod.None -> 0.0
            DiscountCalcMethod.Percentage -> discount.overallDiscountValue
            DiscountCalcMethod.Whole -> discount.overallDiscountValue / fullPrice
        }

        val feeAsPercentage = when (fee.method) {
            PaymentFeeCalcMethod.None -> 0.0
            PaymentFeeCalcMethod.Percentage -> fee.value
            PaymentFeeCalcMethod.Whole -> fee.value / (fullPrice * (1 - discountAsPercentage))
        }

        return fullPrice * (1 - discountAsPercentage) * (1 + feeAsPercentage)
    }
}
