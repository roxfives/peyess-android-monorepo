package com.peyess.salesapp.data.model.sale.service_order.products_sold

import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

data class ProductSoldEyeSetDocument(
    val lenses: ProductSoldDescriptionDocument = ProductSoldDescriptionDocument(),
    val colorings: ProductSoldDescriptionDocument = ProductSoldDescriptionDocument(),
    val treatments: ProductSoldDescriptionDocument = ProductSoldDescriptionDocument(),
)
