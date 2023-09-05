package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSAccessoryItem
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.utils.extentions.roundToDouble

fun ProductSoldDescriptionDocument.toFSSoldProductDescription(
    roundValues: Boolean = false,
): FSProductSoldDescription {
    return FSProductSoldDescription(
        id = id,
        units = units,
        nameDisplay = nameDisplay,
        price = price.roundToDouble(roundValues),
        discount = discount.toFSDiscountDescription(),

        isDiscounted = isDiscounted,
        isIncluded = isIncluded,

        accessoryPerUnit = accessoryPerUnit.map { it.toFSAccessoryItem() },

        supplierId = supplierId,
        supplierName = supplierName,
    )
}