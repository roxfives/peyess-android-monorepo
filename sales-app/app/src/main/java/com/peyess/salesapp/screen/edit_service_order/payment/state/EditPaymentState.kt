package com.peyess.salesapp.screen.edit_service_order.payment.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchTotalResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedFetchResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientReadSingleResponse
import com.peyess.salesapp.repository.payments.PaymentMethodsResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.feature.payment.model.Client
import com.peyess.salesapp.feature.payment.model.Coloring
import com.peyess.salesapp.feature.payment.model.Frames
import com.peyess.salesapp.feature.payment.model.Lens
import com.peyess.salesapp.feature.payment.model.OverallDiscount
import com.peyess.salesapp.feature.payment.model.Payment
import com.peyess.salesapp.feature.payment.model.PaymentFee
import com.peyess.salesapp.feature.payment.model.PaymentMethod
import com.peyess.salesapp.feature.payment.model.Treatment

data class EditPaymentState(
    val paymentId: Long = 0L,
    val clientId: String = "",
    val saleId: String = "",
    val serviceOrderId: String = "",

    val discountAsync: Async<EditPaymentDiscountFetchResponse> = Uninitialized,
    val discount: OverallDiscount = OverallDiscount(),

    val paymentFeeAsync: Async<EditPaymentFeeFetchResponse> = Uninitialized,
    val paymentFee: PaymentFee = PaymentFee(),

    val productPickedResponseAsync: Async<EditProductPickedFetchResponse> = Uninitialized,
    val productPicked: ProductPickedDocument = ProductPickedDocument(),

    val framesResponseAsync: Async<EditFramesFetchResponse> = Uninitialized,
    val frames: Frames = Frames(),

    val lensResponseAsync: Async<SingleLensResponse> = Uninitialized,
    val lens: Lens = Lens(),

    val coloringResponseAsync: Async<SingleColoringResponse> = Uninitialized,
    val coloring: Coloring = Coloring(),

    val treatmentResponseAsync: Async<SingleTreatmentResponse> = Uninitialized,
    val treatment: Treatment = Treatment(),

    val paymentMethodsResponseAsync: Async<PaymentMethodsResponse> = Uninitialized,
    val paymentMethods: List<PaymentMethod> = emptyList(),

    val clientResponseAsync: Async<LocalClientReadSingleResponse> = Uninitialized,
    val client: Client = Client(),

    val paymentResponseAsync: Async<EditLocalPaymentFetchResponse> = Uninitialized,
    val paymentInput: Payment = Payment(),

    val cardFlagsAsync: Async<List<CardFlagDocument>> = Uninitialized,

    val totalPaymentResponseAsync: Async<EditLocalPaymentFetchTotalResponse> = Uninitialized,
    val totalToPay: Double = 0.0,
    val totalPaid: Double = 0.0,
    val totalLeftToPay: Double = 0.0,
): MavericksState {
    val isClientLoading = clientResponseAsync is Loading
}
