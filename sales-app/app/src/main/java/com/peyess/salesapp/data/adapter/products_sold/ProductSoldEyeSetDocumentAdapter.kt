package com.peyess.salesapp.data.adapter.products_sold

import com.peyess.salesapp.data.adapter.product_sold_desc.toFSLensSoldDescription
import com.peyess.salesapp.data.adapter.product_sold_desc.toFSSoldProductDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument

fun ProductSoldEyeSetDocument.toFSProductSoldEyeSet(): FSProductSoldEyeSet {
    return FSProductSoldEyeSet(
        lenses = lenses.toFSLensSoldDescription(),
        treatments = treatments.toFSSoldProductDescription(),
        colorings = colorings.toFSSoldProductDescription(),
    )
}