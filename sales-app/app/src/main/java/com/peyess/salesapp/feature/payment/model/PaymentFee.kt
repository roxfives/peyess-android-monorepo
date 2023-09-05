package com.peyess.salesapp.feature.payment.model

import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.math.BigDecimal

data class PaymentFee(
    val saleId: String = "",
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    val value: BigDecimal = BigDecimal.ZERO,
)
