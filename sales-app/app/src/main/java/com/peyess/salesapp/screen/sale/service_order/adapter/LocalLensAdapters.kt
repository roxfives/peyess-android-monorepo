package com.peyess.salesapp.data.adapter.products

import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.AccessoryItemDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.LensSoldDescriptionDocument
import kotlin.math.max

fun StoreLensWithDetailsDocument.toDescription(
    withTreatment: Boolean,
    withColoring: Boolean,
    withHeight: Double,
    accessoriesPerUnit: List<AccessoryItemDocument>,
): LensSoldDescriptionDocument {
    val hasAltHeight = altHeights.isNotEmpty()
    val altHeight = if (hasAltHeight) {
        findBestHeightForPrescription(withHeight, altHeights)
    } else {
        null
    }

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

        withAltHeight = hasAltHeight,
        altHeightId = altHeight?.id ?: "",
        altHeightDesc = altHeight?.nameDisplay ?: "",
        altHeightValue = altHeight?.value ?: 0.0,

        withTreatment = withTreatment,
        withColoring = withColoring,
        withCut = false,
    )
}

fun findBestHeightForPrescription(
    height: Double,
    heights: List<StoreLensAltHeightDocument>,
): StoreLensAltHeightDocument {
    val sortedHeights = heights.sortedBy { it.value }

    val pickedHeight = sortedHeights.firstOrNull { it.value >= height }
    return pickedHeight ?: sortedHeights.last()
}
