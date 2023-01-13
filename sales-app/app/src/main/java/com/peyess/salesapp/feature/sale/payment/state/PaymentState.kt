package com.peyess.salesapp.feature.sale.payment.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentTotalResponse
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.feature.sale.payment.model.Coloring
import com.peyess.salesapp.feature.sale.payment.model.Frames
import com.peyess.salesapp.feature.sale.payment.model.Lens
import com.peyess.salesapp.feature.sale.payment.model.OverallDiscount
import com.peyess.salesapp.feature.sale.payment.model.Payment
import com.peyess.salesapp.feature.sale.payment.model.PaymentFee
import com.peyess.salesapp.feature.sale.payment.model.Treatment
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import timber.log.Timber

data class PaymentState(
    val paymentId: Long = 0L,
    val clientId: String = "",
    val saleId: String = "",
    val serviceOrderId: String = "",

    val discountAsync: Async<OverallDiscountRepositoryResponse> = Uninitialized,
    val discount: OverallDiscount = OverallDiscount(),

    val paymentFeeAsync: Async<PaymentFeeRepositoryResponse> = Uninitialized,
    val paymentFee: PaymentFee = PaymentFee(),

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

    val saleIdAsync: Async<ActiveSalesEntity?> = Uninitialized,

    val paymentMethodsAsync: Async<List<PaymentMethod>> = Uninitialized,
    val activePaymentMethod: PaymentMethod? = null,

    val clientAsync: Async<ClientDocument?> = Uninitialized,

    val paymentAsync: Async<SalePaymentEntity?> = Uninitialized,

    val cardFlagsAsync: Async<List<CardFlagDocument>> = Uninitialized,

    val totalPaymentResponseAsync: Async<SalePaymentTotalResponse> = Uninitialized,
    val totalToPay: Double = 0.0,

    val totalPaid: Double = 0.0,

    val totalLeftToPay: Double = 0.0,
): MavericksState {
    val arePaymentsLoading = paymentMethodsAsync is Loading
    val paymentMethods = if (paymentMethodsAsync is Success) {
        Timber.i("Setting payment methods ${paymentMethodsAsync.invoke()}")
        paymentMethodsAsync.invoke()
    } else {
        Timber.i("Setting empty payment methods")
        emptyList()
    }

    val areCardFlagsLoading = cardFlagsAsync is Loading
    val cardFlags = cardFlagsAsync.invoke() ?: emptyList()

    val isClientLoading = clientAsync is Loading
    val client = if (clientAsync is Success && clientAsync.invoke() != null) {
        clientAsync.invoke()!!
    } else {
        ClientDocument()
    }

    val isPaymentLoading = paymentAsync is Loading
    val payment = if (paymentAsync is Success && paymentAsync.invoke() != null) {
        paymentAsync.invoke()!!
    } else {
        SalePaymentEntity()
    }
}