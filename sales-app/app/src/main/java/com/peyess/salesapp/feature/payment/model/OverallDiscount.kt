package com.peyess.salesapp.feature.payment.model

import com.peyess.salesapp.typing.products.DiscountCalcMethod

data class OverallDiscount(
    val saleId: String = "",
    val discountMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    val overallDiscountValue: Double = 0.0,
)
