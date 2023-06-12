package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun StoreLensWithDetailsDocument.toDescription(): ProductSoldDescriptionDocument {

    return ProductSoldDescriptionDocument(
        id = id,
        units = 1,
        nameDisplay = name,
        price = price / 2.0,
        discount = DiscountDescriptionDocument(),

        accessoryPerUnit = emptyList(),

        supplierId = supplierId,
        supplierName = supplierName,
    )
}
