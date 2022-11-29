package com.peyess.salesapp.feature.sale.discount.model

import com.peyess.salesapp.typing.products.DiscountCalcMethod

private const val defaultDiscountValue = 0.0

data class Discount(
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    val percentValue: Double = 0.0,
    val wholeValue: Double = 0.0,
) {
    val value = when (method) {
        DiscountCalcMethod.Percentage -> percentValue
        DiscountCalcMethod.Whole -> wholeValue
        DiscountCalcMethod.None -> defaultDiscountValue
    }
}
