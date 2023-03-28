package com.peyess.salesapp.data.adapter.purchase.discount.product_discount_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.DiscountEyeSetDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.FSDiscountEyeSet

fun DiscountEyeSetDocument.toFSDiscountEyeSet(): FSDiscountEyeSet {
    return FSDiscountEyeSet(
        lenses = lenses.toFSDiscountDescription(),
        colorings = colorings.toFSDiscountDescription(),
        treatments = treatments.toFSDiscountDescription(),
    )
}
