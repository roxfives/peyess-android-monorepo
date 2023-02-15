package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.feature.service_order.model.OverallDiscount

fun OverallDiscountDocument.toOverallDiscount(): OverallDiscount {
    return OverallDiscount(
        saleId = saleId,
        discountMethod = discountMethod,
        overallDiscountValue = overallDiscountValue,
    )
}