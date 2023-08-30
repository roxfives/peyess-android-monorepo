package com.peyess.salesapp.feature.payment_discount.model

import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

data class Discount(
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    val percentValue: BigDecimal = BigDecimal.ZERO,
    val wholeValue: BigDecimal = BigDecimal.ZERO,
) {
    val value: BigDecimal = when (method) {
        DiscountCalcMethod.Percentage -> percentValue
        DiscountCalcMethod.Whole -> wholeValue
        DiscountCalcMethod.None -> BigDecimal.ZERO
    }
}
