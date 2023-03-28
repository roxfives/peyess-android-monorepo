package com.peyess.salesapp.data.model.sale.purchase.discount.group.sets

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument

data class SetDiscountDocument(
    val lenses: Map<String, DiscountDescriptionDocument> = mapOf(),
    val colorings: Map<String, DiscountDescriptionDocument> = mapOf(),
    val treatments: Map<String, DiscountDescriptionDocument> = mapOf(),
    val frames: Map<String, DiscountDescriptionDocument> = mapOf(),
    val contacts: Map<String, DiscountDescriptionDocument> = mapOf(),
    val others: Map<String, DiscountDescriptionDocument> = mapOf(),
)
