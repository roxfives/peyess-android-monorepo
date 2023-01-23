package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring.name
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun LocalLensColoringDocument.toDescription(
    isIncluded: Boolean,
    isDiscounted: Boolean,
): ProductSoldDescriptionDocument {
    // TODO: update to local price
    val price = if (isDiscounted || isIncluded) {
        0.0
    } else {
        price / 2.0
    }

    return ProductSoldDescriptionDocument(
        id = id,
        units = 1,
        nameDisplay = name,
        price = price,
        discount = DiscountDescriptionDocument(),

        isIncluded = isIncluded,
        isDiscounted = isDiscounted,
    )
}