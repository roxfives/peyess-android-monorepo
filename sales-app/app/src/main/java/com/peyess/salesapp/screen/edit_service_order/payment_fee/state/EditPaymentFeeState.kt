package com.peyess.salesapp.screen.edit_service_order.payment_fee.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeFetchResponse
import com.peyess.salesapp.feature.payment_fee.model.PaymentFee
import java.math.BigDecimal

data class EditPaymentFeeState(
    val saleId: String = "",
    val fullPrice: BigDecimal = BigDecimal(0.0),

    val paymentFeeResponseAsync: Async<EditPaymentFeeFetchResponse> = Uninitialized,
    val currentPaymentFee: PaymentFee = PaymentFee(),

    val pricePreview: BigDecimal = BigDecimal(0),
): MavericksState
