package com.peyess.salesapp.data.model.sale.service_order.products_sold_desc

import com.peyess.salesapp.dao.sale.frames.FramesType
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument

data class ProductSoldFramesDescriptionDocument(
    val id: String = "",

    val design: String = "",
    val reference: String = "",
    val color: String = "",
    val style: String = "",
    val type: FramesType = FramesType.None,

    val units: Int = 0,
    val price: Double = 0.0,
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
) {
    val nameDisplay = "$reference $design (${type.toName()})"
}
