package com.peyess.salesapp.data.adapter.purchase_discount_desc

import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.FSDiscountDescription

fun DiscountDescriptionDocument.toFSDiscountDescription(): FSDiscountDescription {
    return FSDiscountDescription(
        method = method.toName(),
        value = value,
    )
}

