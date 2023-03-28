package com.peyess.salesapp.data.adapter.purchase.discount.group.standard

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.FSStandardDiscount
import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.StandardDiscountDocument

fun StandardDiscountDocument.toFSStandardDiscount(): FSStandardDiscount {
    return FSStandardDiscount(
        lenses = lenses.toFSDiscountDescription(),
        colorings = colorings.toFSDiscountDescription(),
        treatments = treatments.toFSDiscountDescription(),
        frames = frames.toFSDiscountDescription(),
        contacts = contacts.toFSDiscountDescription(),
        others = others.toFSDiscountDescription(),
    )
}