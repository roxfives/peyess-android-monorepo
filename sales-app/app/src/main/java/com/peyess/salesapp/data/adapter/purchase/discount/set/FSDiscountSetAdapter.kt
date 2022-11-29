package com.peyess.salesapp.data.adapter.purchase.discount

import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.set.DiscountSetDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.set.FSDiscountSet

fun FSDiscountSet.toDiscountSetDocument(): DiscountSetDocument {
    return DiscountSetDocument(
        query = query,
        discount = discount.toDiscountDescriptionDocument(),
    )
}