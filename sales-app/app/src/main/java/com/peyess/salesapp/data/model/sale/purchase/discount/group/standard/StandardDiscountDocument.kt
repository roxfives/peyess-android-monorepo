package com.peyess.salesapp.data.model.sale.purchase.discount.group.standard

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument

data class StandardDiscountDocument(
    val lenses: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val colorings: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val treatments: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val frames: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val contacts: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val others: DiscountDescriptionDocument = DiscountDescriptionDocument(),
)
