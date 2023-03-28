package com.peyess.salesapp.data.adapter.products_sold

import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductsSold
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductsSoldDocument
import com.peyess.salesapp.data.adapter.product_sold_desc.toFSSoldProductDescription
import com.peyess.salesapp.data.adapter.product_sold_desc.toProductSoldDescriptionDocument

fun FSProductsSold.toProductsSoldDocument(): ProductsSoldDocument {
    return ProductsSoldDocument(
        lenses = lenses.mapValues { it.value.toProductSoldDescriptionDocument() },
        colorings = colorings.mapValues { it.value.toProductSoldDescriptionDocument() },
        treatments = treatments.mapValues { it.value.toProductSoldDescriptionDocument() },
        frames = frames.toProductSoldDescriptionDocument(),
        misc = misc.mapValues { it.value.toProductSoldDescriptionDocument() },
    )
}