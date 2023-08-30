package com.peyess.salesapp.feature.service_order.model

import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

data class OverallDiscount(
    val saleId: String = "",
    val discountMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    val overallDiscountValue: BigDecimal = BigDecimal.ZERO,
)
