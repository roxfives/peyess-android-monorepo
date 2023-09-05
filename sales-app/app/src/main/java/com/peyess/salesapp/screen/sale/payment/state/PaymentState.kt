package com.peyess.salesapp.screen.sale.payment.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientReadSingleResponse
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentTotalResponse
import com.peyess.salesapp.data.repository.local_sale.payment.LocalPaymentUpdateResult
import com.peyess.salesapp.data.repository.local_sale.payment.SinglePaymentResponse
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.feature.payment.model.Client
import com.peyess.salesapp.feature.payment.model.Coloring
import com.peyess.salesapp.feature.payment.model.Frames
import com.peyess.salesapp.feature.payment.model.Lens
import com.peyess.salesapp.feature.payment.model.OverallDiscount
import com.peyess.salesapp.feature.payment.model.Payment
import com.peyess.salesapp.feature.payment.model.PaymentFee
import com.peyess.salesapp.feature.payment.model.PaymentMethod
import com.peyess.salesapp.feature.payment.model.Treatment
import com.peyess.salesapp.repository.payments.PaymentMethodsResponse
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import java.math.BigDecimal

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

    val paymentMethodsResponseAsync: Async<PaymentMethodsResponse> = Uninitialized,
    val paymentMethods: List<PaymentMethod> = emptyList(),

    val clientResponseAsync: Async<LocalClientReadSingleResponse> = Uninitialized,
    val client: Client = Client(),

    val finishPaymentResponseAsync: Async<LocalPaymentUpdateResult> = Uninitialized,

    val paymentResponseAsync: Async<SinglePaymentResponse> = Uninitialized,
    val paymentInput: Payment = Payment(),

    val cardFlagsAsync: Async<List<CardFlagDocument>> = Uninitialized,

    val totalPaymentResponseAsync: Async<LocalPaymentTotalResponse> = Uninitialized,

    val totalToPay: BigDecimal = BigDecimal.ZERO,
    val totalPaid: BigDecimal = BigDecimal.ZERO,
    val totalLeftToPay: BigDecimal = BigDecimal.ZERO,

    val finishedPayment: Boolean = false,
): MavericksState {
    val activePaymentMethod = paymentMethods
        .firstOrNull { it.id == paymentInput.methodId }
        ?: PaymentMethod()

    val hasPaymentUpdateFailed = finishPaymentResponseAsync is Fail

    val arePaymentsLoading = paymentMethodsResponseAsync is Loading

    val areCardFlagsLoading = cardFlagsAsync is Loading
    val cardFlags = cardFlagsAsync.invoke() ?: emptyList()

    val isClientLoading = clientResponseAsync is Loading
}