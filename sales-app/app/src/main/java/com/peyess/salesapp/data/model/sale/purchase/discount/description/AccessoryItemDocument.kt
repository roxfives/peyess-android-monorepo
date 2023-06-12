package com.peyess.salesapp.data.model.sale.purchase.discount.description

data class AccessoryItemDocument(
    val nameDisplay: String = "",
    val price: Double = 0.0,
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
)
