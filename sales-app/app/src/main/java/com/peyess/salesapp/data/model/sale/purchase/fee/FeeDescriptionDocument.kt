package com.peyess.salesapp.data.model.sale.purchase.fee

import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.math.BigDecimal

data class FeeDescriptionDocument(
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    val value: BigDecimal = BigDecimal(0.0),
)