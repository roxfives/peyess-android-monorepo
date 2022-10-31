package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.dao.sale.frames.FramesType
import com.peyess.salesapp.data.adapter.purchase_discount_desc.toDiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldFramesDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument

fun FSProductSoldFramesDescription
        .toProductSoldFramesDescriptionDocument(): ProductSoldFramesDescriptionDocument {
    return ProductSoldFramesDescriptionDocument(
        id = id,
        design = design,
        reference = reference,
        color = color,
        style = style,
        type = FramesType.toFramesType(type),
        units = units,
        price = price,
        discount = discount.toDiscountDescriptionDocument(),
    )
}