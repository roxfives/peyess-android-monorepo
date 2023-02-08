package com.peyess.salesapp.data.adapter.edit_service_order.payment_discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.edit_service_order.payment_discount.EditOverallDiscountEntity

fun EditOverallDiscountEntity.toOverallDiscountDocument(): OverallDiscountDocument {
    return OverallDiscountDocument(
        saleId = saleId,
        discountMethod = method,
        overallDiscountValue = value,
    )
}
