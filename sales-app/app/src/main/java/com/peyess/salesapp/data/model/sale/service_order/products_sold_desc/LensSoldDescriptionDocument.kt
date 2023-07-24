package com.peyess.salesapp.data.model.sale.service_order.products_sold_desc

import com.peyess.salesapp.data.model.sale.purchase.discount.description.AccessoryItemDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument

data class LensSoldDescriptionDocument(
    val id: String = "",

    val units: Int = 0,

    val nameDisplay: String = "",

    val price: Double = 0.0,
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),

    val isDiscounted: Boolean = false,
    val isIncluded: Boolean = false,

    val accessoryPerUnit: List<AccessoryItemDocument> = emptyList(),

    val supplierId: String = "",
    val supplierName: String = "",

    val withAltHeight: Boolean = false,
    val altHeightId: String = "",
    val altHeightDesc: String = "",

    val withTreatment: Boolean = false,
    val withColoring: Boolean = false,
    val withCut: Boolean = false,
)
