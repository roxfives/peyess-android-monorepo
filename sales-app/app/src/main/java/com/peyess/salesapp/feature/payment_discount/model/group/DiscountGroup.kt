package com.peyess.salesapp.feature.payment_discount.model.group

import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument

data class DiscountGroup(
    val name: String = "",
    val description: String = "",

    val general: DiscountDescription = DiscountDescription(),
    val standard: StandardDiscount = StandardDiscount(),
)

fun DiscountGroup.toDiscountGroupDocument(): DiscountGroupDocument {
    return DiscountGroupDocument(
        name = name,
        description = description,

        general = general.toDiscountDescriptionDocument(),
        standard = standard.toStandardDiscountDocument(),
    )
}

fun DiscountGroupDocument.toDiscountGroup(): DiscountGroup {
    return DiscountGroup(
        name = name,
        description = description,

        general = general.toDiscountDescription(),
        standard = standard.toStandardDiscount(),
    )
}