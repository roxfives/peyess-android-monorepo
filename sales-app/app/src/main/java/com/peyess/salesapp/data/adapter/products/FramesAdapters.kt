package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames.name
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun FramesEntity.toDescription(): ProductSoldDescriptionDocument {

    // TODO: update to local price
    return ProductSoldDescriptionDocument(
        id = "ref: $reference",
        units = 1,
        nameDisplay = name(),
        price = if (areFramesNew) {
            value
        } else {
            0.0
        },
        discount = DiscountDescriptionDocument(),
    )
}
