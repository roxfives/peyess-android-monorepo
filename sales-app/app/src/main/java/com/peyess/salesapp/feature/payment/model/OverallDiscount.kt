package com.peyess.salesapp.feature.payment.model

import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

data class OverallDiscount(
    val saleId: String = "",
    val discountMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    val overallDiscountValue: BigDecimal = BigDecimal.ZERO,
)
