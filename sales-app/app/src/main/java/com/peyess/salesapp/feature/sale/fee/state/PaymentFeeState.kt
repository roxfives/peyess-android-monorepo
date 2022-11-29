package com.peyess.salesapp.feature.sale.fee.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.feature.sale.fee.model.PaymentFee
import com.peyess.salesapp.feature.sale.fee.model.toPaymentFee
import java.math.BigDecimal

data class PaymentFeeState(
    val currentFeeAsync: Async<PaymentFeeDocument?> = Uninitialized,

    val originalPrice: BigDecimal = BigDecimal(0),
    val pricePreview: BigDecimal = BigDecimal(0),

    val saleId: String = "",
): MavericksState {
    val isPaymentFeeLoading: Boolean = currentFeeAsync is Loading
    val paymentFeeHasError: Boolean = currentFeeAsync is Fail
    val fee: PaymentFee = currentFeeAsync.invoke()?.toPaymentFee()?.copy() ?: PaymentFee()
}