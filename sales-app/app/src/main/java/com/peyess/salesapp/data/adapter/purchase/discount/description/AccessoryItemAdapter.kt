package com.peyess.salesapp.data.adapter.purchase.discount.description

import com.peyess.salesapp.data.model.sale.purchase.discount.description.AccessoryItemDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSAccessoryItem
import com.peyess.salesapp.utils.extentions.roundToDouble

fun AccessoryItemDocument.toFSAccessoryItem(
    roundValues: Boolean = false,
): FSAccessoryItem {
    return FSAccessoryItem(
        nameDisplay = nameDisplay,
        price = price.roundToDouble(roundValues),
        discount = discount.toFSDiscountDescription(),
    )
}