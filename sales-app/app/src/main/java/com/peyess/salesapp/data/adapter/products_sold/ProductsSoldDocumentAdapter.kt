package com.peyess.salesapp.data.adapter.products_sold

import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductsSold
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductsSoldDocument
import com.peyess.salesapp.data.adapter.product_sold_desc.toFSSoldProductDescription

fun ProductsSoldDocument.toFSProductsSold(): FSProductsSold {
    return FSProductsSold(
        lenses = lenses.mapValues { it.value.toFSSoldProductDescription() },
        colorings = colorings.mapValues { it.value.toFSSoldProductDescription() },
        treatments = treatments.mapValues { it.value.toFSSoldProductDescription() },
        frames = frames.toFSSoldProductDescription(),
        misc = misc.mapValues { it.value.toFSSoldProductDescription() },
    )
}