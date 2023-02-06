package com.peyess.salesapp.screen.sale.payment.model

import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

data class PaymentFee(
    val saleId: String = "",
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    val value: Double = 0.0,
)
