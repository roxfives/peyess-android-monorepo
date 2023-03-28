package com.peyess.salesapp.data.model.payment_fee

import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

data class PaymentFeeDocument(
    val saleId: String = "",
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    val value: Double = 0.0,
)
