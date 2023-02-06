package com.peyess.salesapp.screen.sale.payment.adapter

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.screen.sale.payment.model.OverallDiscount

fun OverallDiscountDocument.toOverallDiscount(): OverallDiscount {
    return OverallDiscount(
        saleId = saleId,
        discountMethod = discountMethod,
        overallDiscountValue = overallDiscountValue,
    )
}