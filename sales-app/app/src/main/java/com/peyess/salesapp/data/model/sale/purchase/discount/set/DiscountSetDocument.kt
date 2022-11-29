package com.peyess.salesapp.data.model.sale.purchase.discount.set

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument

data class DiscountSetDocument(
    val query: Map<String, String> = mapOf(),
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
)
