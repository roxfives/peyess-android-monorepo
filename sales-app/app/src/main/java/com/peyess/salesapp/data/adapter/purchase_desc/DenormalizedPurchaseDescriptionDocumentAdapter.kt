package com.peyess.salesapp.data.adapter.purchase_desc

import com.peyess.salesapp.data.model.sale.purchase.DenormalizedPurchaseDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedPurchaseDescription

fun DenormalizedPurchaseDescriptionDocument.toFSDenormalizedPurchaseDescription(): FSDenormalizedPurchaseDescription {
    return FSDenormalizedPurchaseDescription(
        id = id,
        units = units,
        description = description,
    )
}
