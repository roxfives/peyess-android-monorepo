package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames.FramesType
import com.peyess.salesapp.dao.sale.frames.name
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument

fun FramesEntity.toDescription(): ProductSoldFramesDescriptionDocument {
    // TODO: update to local price
    return ProductSoldFramesDescriptionDocument(
        id = "ref: $reference",
        units = 1,
        price = if (areFramesNew) {
            value
        } else {
            0.0
        },
        discount = DiscountDescriptionDocument(),
        design = description,
        reference = reference,
        color = "",
        style = "",
        type = type,
    )
}
