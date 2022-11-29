package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_treatment.name
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun LocalTreatmentEntity.toDescription(): ProductSoldDescriptionDocument {

    // TODO: update to local price
    return ProductSoldDescriptionDocument(
        id = id,
        units = 1,
        nameDisplay = name(),
        price = suggestedPrice / 2.0,
        discount = DiscountDescriptionDocument(),
    )
}
