package com.peyess.salesapp.data.adapter.product_sold_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSAccessoryItem
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSLensSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.LensSoldDescriptionDocument

fun LensSoldDescriptionDocument.toFSLensSoldDescription(): FSLensSoldDescription {
    return FSLensSoldDescription(
        id = id,
        units = units,
        nameDisplay = nameDisplay,
        price = price,
        discount = discount.toFSDiscountDescription(),

        isDiscounted = isDiscounted,
        isIncluded = isIncluded,

        accessoryPerUnit = accessoryPerUnit.map { it.toFSAccessoryItem() },

        supplierId = supplierId,
        supplierName = supplierName,

        withAltHeight = withAltHeight,
        altHeightId = altHeightId,
        altHeightDesc = altHeightDesc,
        altHeightValue = altHeightValue,

        withTreatment = withTreatment,
        withColoring = withColoring,
        withCut = withCut,
    )
}