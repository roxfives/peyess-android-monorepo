package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring.name
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun LocalColoringEntity.toDescription(): ProductSoldDescriptionDocument {

    // TODO: update to local price
    return ProductSoldDescriptionDocument(
        id = id,
        units = 1,
        nameDisplay = name(),
        price = suggestedPrice / 2.0,
        discount = DiscountDescriptionDocument(),
    )
}