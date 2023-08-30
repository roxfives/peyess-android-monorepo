package com.peyess.salesapp.data.model.sale.service_order.products_sold_desc

import com.peyess.salesapp.data.model.sale.purchase.discount.description.AccessoryItemDocument
import com.peyess.salesapp.typing.frames.FramesType
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import java.math.BigDecimal

data class ProductSoldFramesDescriptionDocument(
    val id: String = "",

    val info: String = "",

    val code: String = "",
    val design: String = "",
    val reference: String = "",
    val color: String = "",
    val style: String = "",
    val type: FramesType = FramesType.None,

    val units: Int = 0,
    val price: BigDecimal = BigDecimal.ZERO,
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),

    val accessoriesPerUnit: List<AccessoryItemDocument> = emptyList(),
) {
    val nameDisplay = "$reference $design (${type.toName()})"
}
