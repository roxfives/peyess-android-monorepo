package com.peyess.salesapp.data.adapter.discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.discount.OverallDiscountEntity
import java.math.BigDecimal

fun OverallDiscountEntity.toOverallDiscountDocument(): OverallDiscountDocument {
    return OverallDiscountDocument(
        saleId = saleId,
        discountMethod = method,
        overallDiscountValue = value.toBigDecimal(),
    )
}