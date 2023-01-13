package com.peyess.salesapp.feature.sale.service_order.model

import com.peyess.salesapp.typing.products.DiscountCalcMethod

data class OverallDiscount(
    val saleId: String = "",
    val discountMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    val overallDiscountValue: Double = 0.0,
)
