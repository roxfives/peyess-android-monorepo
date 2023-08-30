package com.peyess.salesapp.data.adapter.purchase.discount.description

import com.peyess.salesapp.data.model.sale.purchase.discount.description.AccessoryItemDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSAccessoryItem

fun FSAccessoryItem.toAccessoryItemDocument(): AccessoryItemDocument {
    return AccessoryItemDocument(
        nameDisplay = nameDisplay,
        price = price.toBigDecimal(),
        discount = discount.toDiscountDescriptionDocument(),
    )
}