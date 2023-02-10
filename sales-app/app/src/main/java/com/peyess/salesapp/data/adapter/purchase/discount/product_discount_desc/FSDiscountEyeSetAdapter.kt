package com.peyess.salesapp.data.adapter.purchase.discount.product_discount_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.DiscountEyeSetDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.FSDiscountEyeSet

fun FSDiscountEyeSet.toDiscountEyeSetDocument(): DiscountEyeSetDocument {
    return DiscountEyeSetDocument(
        lenses =  lenses.toDiscountDescriptionDocument(),
        colorings = colorings.toDiscountDescriptionDocument(),
        treatments = treatments.toDiscountDescriptionDocument(),
    )
}
