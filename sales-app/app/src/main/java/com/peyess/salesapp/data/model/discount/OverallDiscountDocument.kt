package com.peyess.salesapp.data.model.discount

import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

data class OverallDiscountDocument(
    val saleId: String = "",
    val discountMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    val overallDiscountValue: BigDecimal = BigDecimal.ZERO,
)
