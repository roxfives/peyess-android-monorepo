package com.peyess.salesapp.data.model.sale.purchase.discount.description

import java.math.BigDecimal

data class AccessoryItemDocument(
    val nameDisplay: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
)
