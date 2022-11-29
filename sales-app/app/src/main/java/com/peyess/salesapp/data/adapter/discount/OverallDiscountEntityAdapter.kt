package com.peyess.salesapp.data.adapter.discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.discount.OverallDiscountEntity

fun OverallDiscountDocument.toOverallDiscountEntity(): OverallDiscountEntity {
    return OverallDiscountEntity(
        saleId = saleId,
        method = discountMethod,
        value = overallDiscountValue,
    )
}