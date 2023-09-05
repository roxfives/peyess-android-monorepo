package com.peyess.salesapp.screen.sale.service_order.state

import android.net.Uri
import androidx.annotation.StringRes
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentTotalResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentWriteResult
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningFetchBothResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.data.repository.management_picture_upload.PictureAddResponse
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.feature.service_order.model.Client
import com.peyess.salesapp.feature.lens_suggestion.model.Measuring
import com.peyess.salesapp.feature.lens_suggestion.model.toMeasuring
import com.peyess.salesapp.feature.service_order.model.Coloring
import com.peyess.salesapp.feature.service_order.model.Frames
import com.peyess.salesapp.feature.service_order.model.Lens
import com.peyess.salesapp.feature.service_order.model.OverallDiscount
import com.peyess.salesapp.feature.service_order.model.Payment
import com.peyess.salesapp.feature.service_order.model.PaymentFee
import com.peyess.salesapp.feature.service_order.model.Prescription
import com.peyess.salesapp.feature.service_order.model.Treatment
import com.peyess.salesapp.screen.sale.service_order.utils.SaleDataGenerationResponse
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

data class ServiceOrderState(
    val saleId: String = "",
    val serviceOrderId: String = "",
    val isCreating: Boolean = false,

    val activeStoreIdAsync: Async<String> = Uninitialized,
    val activeStoreId: String = "",

    val userClientAsync: Async<ClientPickedEntity?> = Uninitialized,
    val userClient: Client = Client(),

    val responsibleClientAsync: Async<ClientPickedEntity?> = Uninitialized,
    val responsibleClient: Client = Client(),

    val witnessClientAsync: Async<ClientPickedEntity?> = Uninitialized,
    val witnessClient: Client = Client(),
    val hasWitness: Boolean = false,

    val productPickedResponseAsync: Async<ProductPickedResponse> = Uninitialized,
    val productPicked: ProductPickedDocument = ProductPickedDocument(),

    val framesResponseAsync: Async<LocalFramesRepositoryResponse> = Uninitialized,
    val frames: Frames = Frames(),

    val lensResponseAsync: Async<SingleLensResponse> = Uninitialized,
    val lens: Lens = Lens(),

    val coloringResponseAsync: Async<SingleColoringResponse> = Uninitialized,
    val coloring: Coloring = Coloring(),

    val treatmentResponseAsync: Async<SingleTreatmentResponse> = Uninitialized,
    val treatment: Treatment = Treatment(),

    val positioningLeftAsync: Async<PositioningEntity> = Uninitialized,
    val positioningRightAsync: Async<PositioningEntity> = Uninitialized,

    val paymentsResponseAsync: Async<LocalPaymentResponse> = Uninitialized,
    val payments: List<Payment> = emptyList(),

    val discountAsync: Async<OverallDiscountRepositoryResponse> = Uninitialized,
    val discount: OverallDiscount = OverallDiscount(),

    val paymentFeeAsync: Async<PaymentFeeRepositoryResponse> = Uninitialized,
    val paymentFee: PaymentFee = PaymentFee(),

    val totalToPay: BigDecimal = BigDecimal.ZERO,
    val totalToPayWithDiscountAsync: Async<BigDecimal> = Uninitialized,
    val totalPaidAsync: Async<LocalPaymentTotalResponse> = Uninitialized,
    val totalPaid: BigDecimal = BigDecimal.ZERO,

    val hidServiceOrder: String = "",
    val hidSale: String = "",

    val serviceOrderGenerationResponseAsync: Async<SaleDataGenerationResponse> = Uninitialized,
    val serviceOrderResponse: Pair<ServiceOrderDocument, PurchaseDocument> =
        Pair(ServiceOrderDocument(), PurchaseDocument()),

    val serviceOrderPdfAsync: Async<Uri> = Uninitialized,
    @StringRes
    val serviceOrderPdfErrorMessage: Int = R.string.empty_string,

    val isSOPdfBeingGenerated: Boolean = false,

    val localPrescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val addPrescriptionPictureResponseAsync: Async<PictureAddResponse> = Uninitialized,
    val prescription: Prescription = Prescription(),

    val localPositioningsAsync: Async<LocalPositioningFetchBothResponse> = Uninitialized,
    val addPositioningLeftResponseAsync: Async<PictureAddResponse> = Uninitialized,
    val addPositioningRightResponseAsync: Async<PictureAddResponse> = Uninitialized,

    val creatingNewPaymentAsync: Async<LocalPaymentWriteResult> = Uninitialized,
): MavericksState {
    val isSaleDone: Boolean = serviceOrderGenerationResponseAsync is Success
            && serviceOrderGenerationResponseAsync.invoke().isRight()
            && addPrescriptionPictureResponseAsync is Success
            && addPrescriptionPictureResponseAsync.invoke().isRight()
            && addPositioningLeftResponseAsync is Success
            && addPositioningLeftResponseAsync.invoke().isRight()
            && addPositioningRightResponseAsync is Success
            && addPositioningRightResponseAsync.invoke().isRight()
    val isSaleLoading = serviceOrderGenerationResponseAsync is Loading
            || localPositioningsAsync is Loading
            || localPrescriptionResponseAsync is Loading
            || (serviceOrderGenerationResponseAsync is Success && localPositioningsAsync is Uninitialized)
            || (serviceOrderGenerationResponseAsync is Success && localPrescriptionResponseAsync is Uninitialized)
    val hasSaleFailed = serviceOrderGenerationResponseAsync is Fail
            || (serviceOrderGenerationResponseAsync is Success
                && serviceOrderGenerationResponseAsync.invoke().isLeft())

    val isUserLoading = userClientAsync is Loading
    val isResponsibleLoading = responsibleClientAsync is Loading
    val isWitnessLoading = witnessClientAsync is Loading

    val isPrescriptionLoading = localPrescriptionResponseAsync is Loading

//    val isLensLoading = lensEntityAsync is Loading
//    val lensEntity = if (lensEntityAsync is Success) {
//        lensEntityAsync.invoke()
//    } else {
//        LocalLensEntity()
//    }
//
//    val isColoringLoading = lensEntityAsync is Loading
//    val coloringEntity = if (coloringEntityAsync is Success) {
//        coloringEntityAsync.invoke()
//    } else {
//        LocalColoringEntity()
//    }
//
//    val isTreatmentLoading = treatmentEntityAsync is Loading
//    val treatmentEntity = if (treatmentEntityAsync is Success) {
//        treatmentEntityAsync.invoke()
//    } else {
//        LocalTreatmentEntity()
//    }

    val hasFramesLoaded = framesResponseAsync is Success
            && framesResponseAsync.invoke().isRight()
    val hasFramesFailed = framesResponseAsync is Fail
    val isFramesLoading = framesResponseAsync is Loading

    val hasProductLoaded = productPickedResponseAsync is Success
            && productPickedResponseAsync.invoke().isRight()
    val hasProductFailed = productPickedResponseAsync is Fail
    val isProductLoading = productPickedResponseAsync is Loading

    val hasLensLoaded = lensResponseAsync is Success
            && lensResponseAsync.invoke().isRight()
    val isLensFailed = lensResponseAsync is Fail
    val isLensLoading = lensResponseAsync is Loading

    val hasColoringLoaded = coloringResponseAsync is Success
            && coloringResponseAsync.invoke().isRight()
    val hasColoringFailed = coloringResponseAsync is Fail
    val isColoringLoading = coloringResponseAsync is Loading

    val hasTreatmentLoaded = treatmentResponseAsync is Success
            && treatmentResponseAsync.invoke().isRight()
    val hasTreatmentFailed = treatmentResponseAsync is Fail
    val isTreatmentLoading = treatmentResponseAsync is Loading

    val isPositioningLeftLoading = positioningLeftAsync is Loading
    val measureLeft = if (positioningLeftAsync is Success) {
        positioningLeftAsync.invoke().toMeasuring()
    } else {
        Measuring()
    }

    val isPositioningRightLoading = positioningRightAsync is Loading
    val measureRight = if (positioningRightAsync is Success) {
        positioningRightAsync.invoke().toMeasuring()
    } else {
        Measuring()
    }

    val isPaymentLoading = paymentsResponseAsync is Loading

    val totalToPayWithDiscount = calculateTotalToPayWithDiscount(totalToPay, discount)
    val totalToPayWithFee = calculatePriceWithFee(totalToPayWithDiscount, paymentFee)

    val canAddNewPayment = totalPaid < totalToPayWithFee

    val confirmationMessage = if (totalToPayWithFee <= totalPaid) {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumIntegerDigits = 1

        "Deseja finalizar a compra no valor de ${currencyFormatter.format(totalToPayWithFee)}"
    } else {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumIntegerDigits = 1

        val missing = (totalToPayWithFee - totalPaid).abs()
        "Deseja finalizar a compra no valor de ${currencyFormatter.format(totalToPayWithFee)}" +
                " com o saldo à receber de ${currencyFormatter.format(missing)}"
    }

    val serviceOrderPdf: Uri = serviceOrderPdfAsync.invoke() ?: Uri.EMPTY
    val isServiceOrderPdfLoading = serviceOrderPdfAsync is Loading
    val isServiceOrderPdfSuccess = serviceOrderPdfAsync is Success
    val isServiceOrderPdfError = serviceOrderPdfAsync is Fail

    private fun calculatePriceWithFee(
        price: BigDecimal,
        fee: PaymentFee,
    ): BigDecimal {
        return when (fee.method) {
            PaymentFeeCalcMethod.None -> price
            PaymentFeeCalcMethod.Percentage -> price * (BigDecimal.ONE + fee.value)
            PaymentFeeCalcMethod.Whole -> price + fee.value
        }
    }

    private fun calculateTotalToPayWithDiscount(
        price: BigDecimal,
        discount: OverallDiscount,
    ): BigDecimal {
        return when(discount.discountMethod) {
            DiscountCalcMethod.None -> price
            DiscountCalcMethod.Percentage -> price * (BigDecimal.ONE - discount.overallDiscountValue)
            DiscountCalcMethod.Whole -> price - discount.overallDiscountValue
        }
    }
}
