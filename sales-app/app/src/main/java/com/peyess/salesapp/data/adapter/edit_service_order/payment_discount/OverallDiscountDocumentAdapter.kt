package com.peyess.salesapp.data.adapter.edit_service_order.payment_discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.edit_service_order.payment_discount.EditOverallDiscountEntity

fun OverallDiscountDocument.toEditOverallDiscountEntity(): EditOverallDiscountEntity {
    return EditOverallDiscountEntity(
        saleId = saleId,
        method = discountMethod,
        value = overallDiscountValue
    )
}
