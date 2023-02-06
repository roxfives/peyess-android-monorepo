package com.peyess.salesapp.screen.sale.discount.model.group

import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.StandardDiscountDocument

data class StandardDiscount(
    val lenses: DiscountDescription = DiscountDescription(),
    val colorings: DiscountDescription = DiscountDescription(),
    val treatments: DiscountDescription = DiscountDescription(),
    val frames: DiscountDescription = DiscountDescription(),
    val contacts: DiscountDescription = DiscountDescription(),
    val others: DiscountDescription = DiscountDescription(),
)

fun StandardDiscount.toStandardDiscountDocument(): StandardDiscountDocument {
    return StandardDiscountDocument(
        lenses = lenses.toDiscountDescriptionDocument(),
        colorings = colorings.toDiscountDescriptionDocument(),
        treatments = treatments.toDiscountDescriptionDocument(),
        frames = frames.toDiscountDescriptionDocument(),
        contacts = contacts.toDiscountDescriptionDocument(),
        others = others.toDiscountDescriptionDocument(),
    )
}

fun StandardDiscountDocument.toStandardDiscount(): StandardDiscount {
    return StandardDiscount(
        lenses = lenses.toDiscountDescription(),
        colorings = colorings.toDiscountDescription(),
        treatments = treatments.toDiscountDescription(),
        frames = frames.toDiscountDescription(),
        contacts = contacts.toDiscountDescription(),
        others = others.toDiscountDescription(),
    )
}