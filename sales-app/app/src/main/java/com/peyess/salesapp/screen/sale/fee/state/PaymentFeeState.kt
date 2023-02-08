package com.peyess.salesapp.screen.sale.fee.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.screen.sale.fee.model.PaymentFee
import com.peyess.salesapp.screen.sale.fee.model.toPaymentFee
import java.math.BigDecimal

data class PaymentFeeState(
    val currentFeeAsync: Async<PaymentFeeDocument?> = Uninitialized,
    val currentPaymentFee: PaymentFee = PaymentFee(),

    val originalPrice: BigDecimal = BigDecimal(0),
    val pricePreview: BigDecimal = BigDecimal(0),

    val saleId: String = "",
    val hasFinished: Boolean = false,
): MavericksState {
    val isPaymentFeeLoading: Boolean = currentFeeAsync is Loading
    val paymentFeeHasError: Boolean = currentFeeAsync is Fail
}