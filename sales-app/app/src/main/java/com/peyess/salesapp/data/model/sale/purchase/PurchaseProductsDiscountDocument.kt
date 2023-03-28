package com.peyess.salesapp.data.model.sale.purchase

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.DiscountEyeSetDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc.FSDiscountEyeSet

data class PurchaseProductsDiscountDocument(
    val isOverall: Boolean = false,
    val overall: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val hasOwnFrames: Boolean = false,
    val frames: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val leftProducts: DiscountEyeSetDocument = DiscountEyeSetDocument(),
    val rightProducts: DiscountEyeSetDocument = DiscountEyeSetDocument(),
)
