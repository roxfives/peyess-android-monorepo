package com.peyess.salesapp.data.model.sale.purchase

import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument


data class DenormalizedServiceOrderDescDocument(
    val hasOwnFrames: Boolean = false,

    val leftProducts: ProductSoldEyeSetDocument = ProductSoldEyeSetDocument(),
    val rightProducts: ProductSoldEyeSetDocument = ProductSoldEyeSetDocument(),

    val framesProducts: ProductSoldFramesDescriptionDocument = ProductSoldFramesDescriptionDocument(),

    val miscProducts: List<ProductSoldDescriptionDocument> = emptyList(),
)
