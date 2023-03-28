package com.peyess.salesapp.data.adapter.purchase.discount.set

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.discount.set.DiscountSetDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.set.FSDiscountSet

fun DiscountSetDocument.toFSDiscountSet(): FSDiscountSet {
    return FSDiscountSet(
        query = query,
        discount = discount.toFSDiscountDescription(),
    )
}