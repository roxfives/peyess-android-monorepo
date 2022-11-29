package com.peyess.salesapp.data.adapter.purchase.discount.group.standard

import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.FSStandardDiscount
import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.StandardDiscountDocument

fun FSStandardDiscount.toStandardDiscountDocument(): StandardDiscountDocument {
    return StandardDiscountDocument(
        lenses = lenses.toDiscountDescriptionDocument(),
        colorings = colorings.toDiscountDescriptionDocument(),
        treatments = treatments.toDiscountDescriptionDocument(),
        frames = frames.toDiscountDescriptionDocument(),
        contacts = contacts.toDiscountDescriptionDocument(),
        others = others.toDiscountDescriptionDocument(),
    )
}