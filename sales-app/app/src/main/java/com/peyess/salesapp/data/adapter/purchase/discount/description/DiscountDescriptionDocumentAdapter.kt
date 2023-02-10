package com.peyess.salesapp.data.adapter.purchase.discount.description

import com.peyess.salesapp.data.adapter.purchase.discount.product_discount_desc.toDiscountEyeSetDocument
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseProductsDiscount
import com.peyess.salesapp.data.model.sale.purchase.PurchaseProductsDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

fun DiscountDescriptionDocument.toFSDiscountDescription(): FSDiscountDescription {
    return FSDiscountDescription(
        method = method.toName(),
        value = value.toDouble(),
    )
}

fun FSPurchaseProductsDiscount.toPurchaseProductsDiscountDocument(): PurchaseProductsDiscountDocument {
    return PurchaseProductsDiscountDocument(
        isOverall = isOverall,
        overall = overall.toDiscountDescriptionDocument(),
        hasOwnFrames = hasOwnFrames,
        frames = frames.toDiscountDescriptionDocument(),
        leftProducts = leftProducts.toDiscountEyeSetDocument(),
        rightProducts = rightProducts.toDiscountEyeSetDocument(),
    )
}
