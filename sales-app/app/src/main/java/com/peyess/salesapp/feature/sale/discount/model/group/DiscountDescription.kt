package com.peyess.salesapp.feature.sale.discount.model.group

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal
import java.math.RoundingMode

data class DiscountDescription(
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    val value: BigDecimal = BigDecimal(0.0),
) {
    fun maxValueAsPercentage(fullPrice: BigDecimal): BigDecimal {
        return when (method) {
            DiscountCalcMethod.None -> BigDecimal(0.0)
            DiscountCalcMethod.Percentage -> value
            DiscountCalcMethod.Whole -> value.divide(fullPrice, RoundingMode.HALF_EVEN)
        }
    }

    fun maxValueAsWhole(fullPrice: BigDecimal): BigDecimal {
        return when (method) {
            DiscountCalcMethod.None -> BigDecimal(0.0)
            DiscountCalcMethod.Percentage -> fullPrice * value
            DiscountCalcMethod.Whole -> value
        }
    }
}

fun DiscountDescription.toDiscountDescriptionDocument(): DiscountDescriptionDocument {
    return DiscountDescriptionDocument(
        method = method,
        value = value,
    )
}

fun DiscountDescriptionDocument.toDiscountDescription(): DiscountDescription {
    return DiscountDescription(
        method = method,
        value = value,
    )
}
