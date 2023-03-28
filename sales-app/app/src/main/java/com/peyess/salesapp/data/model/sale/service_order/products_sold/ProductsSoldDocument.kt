package com.peyess.salesapp.data.model.sale.service_order.products_sold

import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

data class ProductsSoldDocument(
    val lenses: Map<String, ProductSoldDescriptionDocument> = emptyMap(),
    val colorings: Map<String, ProductSoldDescriptionDocument> = emptyMap(),
    val treatments: Map<String, ProductSoldDescriptionDocument> = emptyMap(),

    val frames: ProductSoldDescriptionDocument = ProductSoldDescriptionDocument(),

    val misc: Map<String, ProductSoldDescriptionDocument> = emptyMap(),
)
