package com.peyess.salesapp.feature.sale.payment.adapter

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.feature.sale.payment.model.OverallDiscount

fun OverallDiscountDocument.toOverallDiscount(): OverallDiscount {
    return OverallDiscount(
        saleId = saleId,
        discountMethod = discountMethod,
        overallDiscountValue = overallDiscountValue,
    )
}