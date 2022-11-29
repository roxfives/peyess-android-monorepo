package com.peyess.salesapp.data.model.discount

import com.peyess.salesapp.typing.products.DiscountCalcMethod

data class OverallDiscountDocument(
    val saleId: String = "",

    val discountMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,

    val overallDiscountValue: Double = 0.0,
)
