package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.dao.sale.frames.FramesType
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldFramesDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument

fun ProductSoldFramesDescriptionDocument
        .toFSProductSoldFramesDescription(): FSProductSoldFramesDescription {
    return FSProductSoldFramesDescription(
        id = id,
        design = design,
        reference = reference,
        info = info,
        color = color,
        style = style,
        type = FramesType.toName(type),
        units = units,
        price = price,
        discount = discount.toFSDiscountDescription(),
    )
}