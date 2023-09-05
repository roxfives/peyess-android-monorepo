package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toFSProductSoldFramesDescription
import com.peyess.salesapp.data.adapter.product_sold_desc.toFSSoldProductDescription
import com.peyess.salesapp.data.adapter.products_sold.toFSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedServiceOrderDesc

fun DenormalizedServiceOrderDescDocument.toFSDenormalizedServiceOrderDesc(): FSDenormalizedServiceOrderDesc {
    return FSDenormalizedServiceOrderDesc(
        hasOwnFrames = hasOwnFrames,

        leftProducts = leftProducts.toFSProductSoldEyeSet(),
        rightProducts = rightProducts.toFSProductSoldEyeSet(),

        framesProducts = framesProducts.toFSProductSoldFramesDescription(),

        miscProducts = miscProducts.map { it.toFSSoldProductDescription() },

        observation = observation,
        prescriptionId = prescriptionId,
    )
}