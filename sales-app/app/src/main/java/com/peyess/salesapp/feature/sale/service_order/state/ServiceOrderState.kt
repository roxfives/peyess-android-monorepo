package com.peyess.salesapp.feature.sale.service_order.state

import android.net.Uri
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.Measuring
import com.peyess.salesapp.feature.sale.lens_pick.model.toMeasuring
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import timber.log.Timber
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

data class ServiceOrderState(
    val userClientAsync: Async<ClientEntity?> = Uninitialized,
    val responsibleClientAsync: Async<ClientEntity?> = Uninitialized,
    val witnessClientAsync: Async<ClientEntity?> = Uninitialized,

    val prescriptionPictureAsync: Async<PrescriptionPictureEntity> = Uninitialized,
    val prescriptionDataAsync: Async<PrescriptionDataEntity> = Uninitialized,

    val lensEntityAsync: Async<LocalLensEntity> = Uninitialized,
    val coloringEntityAsync: Async<LocalColoringEntity> = Uninitialized,
    val treatmentEntityAsync: Async<LocalTreatmentEntity> = Uninitialized,
    val framesEntityAsync: Async<FramesEntity> = Uninitialized,

    val positioningLeftAsync: Async<PositioningEntity> = Uninitialized,
    val positioningRightAsync: Async<PositioningEntity> = Uninitialized,

    val paymentsAsync: Async<List<SalePaymentEntity>> = Uninitialized,

    val discountAsync: Async<OverallDiscountDocument?> = Uninitialized,
    val paymentFeeAsync: Async<PaymentFeeDocument?> = Uninitialized,

    val totalToPayAsync: Async<Double> = Uninitialized,
    val totalToPayWithDiscountAsync: Async<Double> = Uninitialized,
    val totalPaidAsync: Async<Double> = Uninitialized,

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
    val saleId = saleIdAsync.invoke()?.id ?: ""

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

    val isDiscountLoading = discountAsync is Loading
    val discount = discountAsync.invoke()

    val isPaymentFeeLoading = paymentFeeAsync is Loading
    val paymentFee = paymentFeeAsync.invoke()

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

    val isLensLoading = lensEntityAsync is Loading
    val lensEntity = if (lensEntityAsync is Success) {
        lensEntityAsync.invoke()
    } else {
        LocalLensEntity()
    }

    val isColoringLoading = lensEntityAsync is Loading
    val coloringEntity = if (coloringEntityAsync is Success) {
        coloringEntityAsync.invoke()
    } else {
        LocalColoringEntity()
    }

    val isTreatmentLoading = treatmentEntityAsync is Loading
    val treatmentEntity = if (treatmentEntityAsync is Success) {
        treatmentEntityAsync.invoke()
    } else {
        LocalTreatmentEntity()
    }

    val isFramesLoading = framesEntityAsync is Loading
    val framesEntity = if (framesEntityAsync is Success) {
        framesEntityAsync.invoke()
    } else {
        FramesEntity()
    }

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

    val isPaymentLoading = paymentsAsync is Loading
    val payments = if (paymentsAsync is Success) {
        paymentsAsync.invoke()
    } else {
        emptyList()
    }

    val isTotalPaidLoading = totalPaidAsync is Loading || totalToPayAsync is Loading
    val totalToPay = if (totalToPayAsync is Success) {
        totalToPayAsync.invoke()
    } else {
        0.0
    }
    val totalToPayWithDiscount = if (totalToPayWithDiscountAsync is Success) {
        totalToPayWithDiscountAsync.invoke()
    } else {
        0.0
    }
    val totalPaid = if (totalPaidAsync is Success) {
        totalPaidAsync.invoke()
    } else {
        0.0
    }
    val totalToPayWithFee = if (paymentFee != null) {
        calculatePriceWithFee(totalToPayWithDiscount, paymentFee)
    } else {
        totalToPayWithDiscount
    }

    val canAddNewPayment = totalPaid < totalToPayWithFee

    val confirmationMessage = if (totalToPay <= totalPaid) {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumIntegerDigits = 1

        "Deseja finalizar a compra no valor de ${currencyFormatter.format(totalToPay)}"
    } else {
        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumIntegerDigits = 1

        val missing = abs(totalToPay - totalPaid)

        "Deseja finalizar a compra no valor de ${currencyFormatter.format(totalToPay)}" +
                " com o saldo à receber de ${currencyFormatter.format(missing)}"
    }

    val serviceOrderPdf: Uri = serviceOrderPdfAsync.invoke() ?: Uri.EMPTY
    val isServiceOrderPdfLoading = serviceOrderPdfAsync is Loading
    val isServiceOrderPdfSuccess = serviceOrderPdfAsync is Success
    val isServiceOrderPdfError = serviceOrderPdfAsync is Fail

    private fun calculatePriceWithFee(
        totalToPay: Double,
        fee: PaymentFeeDocument,
    ): Double {
        return when (fee.method) {
            PaymentFeeCalcMethod.None -> totalToPay
            PaymentFeeCalcMethod.Percentage -> totalToPay * (1 + fee.value)
            PaymentFeeCalcMethod.Whole -> totalToPay + fee.value
        }
    }
}
