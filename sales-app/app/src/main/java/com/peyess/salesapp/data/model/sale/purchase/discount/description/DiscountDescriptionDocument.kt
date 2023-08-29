package com.peyess.salesapp.data.model.sale.purchase.discount.description

import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

data class DiscountDescriptionDocument(
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    val value: BigDecimal = BigDecimal.ZERO,
)

fun DiscountDescriptionDocument.toWholeFormat(
    priceWithoutDiscount: BigDecimal,
): DiscountDescriptionDocument {
    return when (method) {
        is DiscountCalcMethod.None -> {
            copy(
                method = DiscountCalcMethod.Percentage,
                value = BigDecimal.ZERO,
            )
        }

        is DiscountCalcMethod.Whole -> {
            copy()
        }

        is DiscountCalcMethod.Percentage -> {
            copy(
                method = DiscountCalcMethod.Whole,
                value = priceWithoutDiscount.multiply(value)
            )
        }
    }
}