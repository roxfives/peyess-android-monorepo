package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring.name
import com.peyess.salesapp.dao.products.room.local_lens.name
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.FSDiscountDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun LocalColoringEntity.toDescription(): ProductSoldDescriptionDocument {

    // TODO: update to local price
    return ProductSoldDescriptionDocument(
        id = id,
        units = 2,
        nameDisplay = name(),
        price = suggestedPrice,
        discount = DiscountDescriptionDocument(),
    )
}