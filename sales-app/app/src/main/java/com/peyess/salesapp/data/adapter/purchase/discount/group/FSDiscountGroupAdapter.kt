package com.peyess.salesapp.data.adapter.purchase.discount.group

import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.adapter.purchase.discount.group.standard.toStandardDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.FSDiscountGroup

fun FSDiscountGroup.toDiscountGroupDocument(): DiscountGroupDocument {
    return DiscountGroupDocument(
        name = name,
        description = description,
        allowedGeneral = allowedGeneral,
        general = general.toDiscountDescriptionDocument(),
        standard = standard.toStandardDiscountDocument(),
        discounts = discounts.toDiscountDescriptionDocument(),
    )
}