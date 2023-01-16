package com.peyess.salesapp.feature.sale.service_order.state

import android.net.Uri
import androidx.annotation.StringRes
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentTotalResponse
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.feature.sale.lens_pick.model.Measuring
import com.peyess.salesapp.feature.sale.lens_pick.model.toMeasuring
import com.peyess.salesapp.feature.sale.service_order.model.Coloring
import com.peyess.salesapp.feature.sale.service_order.model.Frames
import com.peyess.salesapp.feature.sale.service_order.model.Lens
import com.peyess.salesapp.feature.sale.service_order.model.OverallDiscount
import com.peyess.salesapp.feature.sale.service_order.model.Payment
import com.peyess.salesapp.feature.sale.service_order.model.PaymentFee
import com.peyess.salesapp.feature.sale.service_order.model.Treatment
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

data class ServiceOrderState(
    val saleId: String = "",
    val serviceOrderId: String = "",
    val isCreating: Boolean = false,

    val userClientAsync: Async<ClientEntity?> = Uninitialized,
    val responsibleClientAsync: Async<ClientEntity?> = Uninitialized,
    val witnessClientAsync: Async<ClientEntity?> = Uninitialized,

    val prescriptionPictureAsync: Async<PrescriptionPictureEntity> = Uninitialized,
    val prescriptionDataAsync: Async<PrescriptionDataEntity> = Uninitialized,

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

    val paymentsResponseAsync: Async<SalePaymentResponse> = Uninitialized,
    val payments: List<Payment> = emptyList(),

    val discountAsync: Async<OverallDiscountRepositoryResponse> = Uninitialized,
    val discount: OverallDiscount = OverallDiscount(),

    val paymentFeeAsync: Async<PaymentFeeRepositoryResponse> = Uninitialized,
    val paymentFee: PaymentFee = PaymentFee(),

    val totalToPay: Double = 0.0,
    val totalToPayWithDiscountAsync: Async<Double> = Uninitialized,
    val totalPaidAsync: Async<SalePaymentTotalResponse> = Uninitialized,
    val totalPaid: Double = 0.0,

    val hidServiceOrder: String = "",
    val hidSale: String = "",
    val saleIdAsync: Async<ActiveSalesEntity?> = Uninitialized,

    val serviceOrderPdfAsync: Async<Uri> = Uninitialized,
    @StringRes
    val serviceOrderPdfErrorMessage: Int = R.string.empty_string,

    val isSaleDone: Boolean = false,
    val isSaleLoading: Boolean = false,
    val hasSaleFailed: Boolean = false,

    val isSOPdfBeingGenerated: Boolean = false,
): MavericksState {
    val isUserLoading = userClientAsync is Loading
    val userClient = if (userClientAsync is Success) {
        userClientAsync.invoke() ?: ClientEntity()
    } else {
        ClientEntity()
    }

    val isResponsibleLoading = responsibleClientAsync is Loading
    val responsibleClient = if (responsibleClientAsync is Success) {
        responsibleClientAsync.invoke() ?: ClientEntity()
    } else {
        ClientEntity()
    }

    val isWitnessLoading = witnessClientAsync is Loading
    val witnessClient = witnessClientAsync.invoke()

    val isPrescriptionPictureLoading = prescriptionPictureAsync is Loading
    val prescriptionPicture = if (prescriptionPictureAsync is Success) {
        prescriptionPictureAsync.invoke()
    } else {
        PrescriptionPictureEntity()
    }

    val isPrescriptionDataLoading = prescriptionDataAsync is Loading
    val prescriptionData = if (prescriptionDataAsync is Success) {
        prescriptionDataAsync.invoke()
    } else {
        PrescriptionDataEntity()
    }

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

        val missing = abs(totalToPayWithFee - totalPaid)

        "Deseja finalizar a compra no valor de ${currencyFormatter.format(totalToPayWithFee)}" +
                " com o saldo à receber de ${currencyFormatter.format(missing)}"
    }

    val serviceOrderPdf: Uri = serviceOrderPdfAsync.invoke() ?: Uri.EMPTY
    val isServiceOrderPdfLoading = serviceOrderPdfAsync is Loading
    val isServiceOrderPdfSuccess = serviceOrderPdfAsync is Success
    val isServiceOrderPdfError = serviceOrderPdfAsync is Fail

    private fun calculatePriceWithFee(
        price: Double,
        fee: PaymentFee,
    ): Double {
        return when (fee.method) {
            PaymentFeeCalcMethod.None -> price
            PaymentFeeCalcMethod.Percentage -> price * (1 + fee.value)
            PaymentFeeCalcMethod.Whole -> price + fee.value
        }
    }

    private fun calculateTotalToPayWithDiscount(
        price: Double,
        discount: OverallDiscount,
    ): Double {
        return when(discount.discountMethod) {
            DiscountCalcMethod.None -> price
            DiscountCalcMethod.Percentage -> price * (1.0 - discount.overallDiscountValue)
            DiscountCalcMethod.Whole -> price - discount.overallDiscountValue
        }
    }
}
