package com.peyess.salesapp.data.adapter.discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.discount.OverallDiscountEntity
import com.peyess.salesapp.utils.extentions.roundToDouble
import java.math.RoundingMode

fun OverallDiscountDocument.toOverallDiscountEntity(
    roundValue: Boolean = false,
): OverallDiscountEntity {
    return OverallDiscountEntity(
        saleId = saleId,
        method = discountMethod,
        value = overallDiscountValue.roundToDouble(roundValue),
    )
}