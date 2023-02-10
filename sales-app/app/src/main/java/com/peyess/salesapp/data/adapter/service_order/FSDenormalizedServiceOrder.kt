package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toProductSoldDescriptionDocument
import com.peyess.salesapp.data.adapter.product_sold_desc.toProductSoldFramesDescriptionDocument
import com.peyess.salesapp.data.adapter.products_sold.toProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedServiceOrderDesc

fun FSDenormalizedServiceOrderDesc.toDenormalizedServiceOrderDescDocument(): DenormalizedServiceOrderDescDocument {
    return DenormalizedServiceOrderDescDocument(
        hasOwnFrames = hasOwnFrames,

        leftProducts = leftProducts.toProductSoldEyeSetDocument(),
        rightProducts = rightProducts.toProductSoldEyeSetDocument(),

        framesProducts = framesProducts.toProductSoldFramesDescriptionDocument(),

        miscProducts = miscProducts.map { it.toProductSoldDescriptionDocument() },
    )
}