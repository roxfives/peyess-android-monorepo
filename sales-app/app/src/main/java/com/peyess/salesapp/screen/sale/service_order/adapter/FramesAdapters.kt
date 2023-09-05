package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import java.math.BigDecimal

fun FramesDocument.toDescription(): ProductSoldFramesDescriptionDocument {
    // TODO: update to local price
    return ProductSoldFramesDescriptionDocument(
        id = "ref: $reference",
        units = 1,
        price = if (areFramesNew) { value } else { BigDecimal.ZERO },
        discount = DiscountDescriptionDocument(),
        code = tagCode,
        design = design,
        reference = reference,
        info = framesInfo,
        color = "",
        style = "",
        type = type,

        accessoriesPerUnit = emptyList(),
    )
}

fun LocalFramesDocument.toDescription(): ProductSoldFramesDescriptionDocument {
    // TODO: update to local price
    return ProductSoldFramesDescriptionDocument(
        id = "ref: $reference",
        units = 1,
        price = if (areFramesNew) { value } else { BigDecimal.ZERO },
        discount = DiscountDescriptionDocument(),
        code = tagCode,
        design = design,
        reference = reference,
        info = framesInfo,
        color = "",
        style = "",
        type = type,

        accessoriesPerUnit = emptyList(),
    )
}
