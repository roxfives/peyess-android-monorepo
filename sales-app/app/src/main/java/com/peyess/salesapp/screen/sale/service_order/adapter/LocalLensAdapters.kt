package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.AccessoryItemDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.LensSoldDescriptionDocument

fun StoreLensWithDetailsDocument.toDescription(
    withTreatment: Boolean,
    withColoring: Boolean,
    accessoriesPerUnit: List<AccessoryItemDocument>,
): LensSoldDescriptionDocument {

    return LensSoldDescriptionDocument(
        id = id,
        units = 1,
        nameDisplay = name,
        price = price / 2.0,
        discount = DiscountDescriptionDocument(),

        accessoryPerUnit = accessoriesPerUnit,

        supplierId = supplierId,
        supplierName = supplierName,

        isDiscounted = false,
        isIncluded = false,

        withAltHeight = false,
        altHeightId = "",

        withTreatment = withTreatment,
        withColoring = withColoring,
        withCut = false,
    )
}
