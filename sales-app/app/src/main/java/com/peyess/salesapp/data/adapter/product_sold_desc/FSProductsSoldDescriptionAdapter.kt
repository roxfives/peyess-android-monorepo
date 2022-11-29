package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedPurchaseDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun FSProductSoldDescription.toProductSoldDescriptionDocument(): ProductSoldDescriptionDocument {
    return ProductSoldDescriptionDocument(
        id = id,
        units = units,
        nameDisplay = nameDisplay,
        price = price,
        discount = discount.toDiscountDescriptionDocument(),
    )
}

fun FSProductSoldDescription.toFSDenormalizedPurchaseDescription(): FSDenormalizedPurchaseDescription {
    return FSDenormalizedPurchaseDescription(
        id = id,
        units = units,
        description = nameDisplay,
    )
}