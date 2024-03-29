package com.peyess.salesapp.feature.payment_fee.model

import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

private const val defaultDiscountValue = 0.0

data class PaymentFee(
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    val percentValue: Double = 0.0,
    val wholeValue: Double = 0.0,
) {
    val value = when (method) {
        PaymentFeeCalcMethod.Percentage -> percentValue
        PaymentFeeCalcMethod.Whole -> wholeValue
        PaymentFeeCalcMethod.None -> defaultDiscountValue
    }
}
