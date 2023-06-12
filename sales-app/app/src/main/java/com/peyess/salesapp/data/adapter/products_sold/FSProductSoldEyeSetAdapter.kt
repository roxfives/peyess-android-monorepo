package com.peyess.salesapp.data.adapter.products_sold

import com.peyess.salesapp.data.adapter.product_sold_desc.toLensSoldDescriptionDocument
import com.peyess.salesapp.data.adapter.product_sold_desc.toProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument

fun FSProductSoldEyeSet.toProductSoldEyeSetDocument(): ProductSoldEyeSetDocument {
    return ProductSoldEyeSetDocument(
        lenses = lenses.toLensSoldDescriptionDocument(),
        treatments = treatments.toProductSoldDescriptionDocument(),
        colorings = colorings.toProductSoldDescriptionDocument(),
    )
}
