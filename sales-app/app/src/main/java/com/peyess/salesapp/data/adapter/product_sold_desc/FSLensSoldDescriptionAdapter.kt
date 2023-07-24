package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toAccessoryItemDocument
import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSLensSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.LensSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument

fun FSLensSoldDescription.toLensSoldDescriptionDocument(): LensSoldDescriptionDocument {
    return LensSoldDescriptionDocument(
        id = id,
        units = units,
        nameDisplay = nameDisplay,
        price = price,
        discount = discount.toDiscountDescriptionDocument(),

        isDiscounted = isDiscounted,
        isIncluded = isIncluded,

        accessoryPerUnit = accessoryPerUnit.map { it.toAccessoryItemDocument() },

        supplierId = supplierId,
        supplierName = supplierName,

        withAltHeight = withAltHeight,
        altHeightId = altHeightId,
        altHeightDesc = altHeightDesc,

        withTreatment = withTreatment,
        withColoring = withColoring,
        withCut = withCut,
    )
}