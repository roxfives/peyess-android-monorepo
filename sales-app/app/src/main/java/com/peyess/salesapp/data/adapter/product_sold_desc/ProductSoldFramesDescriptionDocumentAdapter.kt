package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.typing.frames.FramesType
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldFramesDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import com.peyess.salesapp.utils.extentions.roundToDouble

fun ProductSoldFramesDescriptionDocument.toFSProductSoldFramesDescription(
    roundValues: Boolean = false,
): FSProductSoldFramesDescription {
    return FSProductSoldFramesDescription(
        id = id,
        design = design,
        code = code,
        reference = reference,
        info = info,
        color = color,
        style = style,
        type = FramesType.toName(type),
        units = units,
        price = price.roundToDouble(roundValues),
        discount = discount.toFSDiscountDescription(),
    )
}