package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.purchase_desc.toFSDenormalizedPurchaseDescription
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedServiceOrderDesc

fun DenormalizedServiceOrderDescDocument.toFSDenormalizedServiceOrderDesc(): FSDenormalizedServiceOrderDesc {
    return FSDenormalizedServiceOrderDesc(
        lenses = lenses.map { it.toFSDenormalizedPurchaseDescription() },
        colorings = colorings.map { it.toFSDenormalizedPurchaseDescription() },
        treatments = treatments.map { it.toFSDenormalizedPurchaseDescription() },

        frames = frames.toFSDenormalizedPurchaseDescription(),

        misc = misc.map { it.toFSDenormalizedPurchaseDescription() },
    )
}