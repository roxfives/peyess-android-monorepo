package com.peyess.salesapp.data.adapter.purchase.discount.description

import com.peyess.salesapp.data.adapter.purchase.discount.product_discount_desc.toFSDiscountEyeSet
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseProductsDiscount
import com.peyess.salesapp.data.model.sale.purchase.PurchaseProductsDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

fun FSDiscountDescription.toDiscountDescriptionDocument(): DiscountDescriptionDocument {
    return DiscountDescriptionDocument(
        method = DiscountCalcMethod.fromName(method),
        value = BigDecimal(value),
    )
}

fun PurchaseProductsDiscountDocument.toFSPurchaseProductsDiscount(): FSPurchaseProductsDiscount {
    return FSPurchaseProductsDiscount(
        isOverall = isOverall,
        overall = overall.toFSDiscountDescription(),
        hasOwnFrames = hasOwnFrames,
        frames = frames.toFSDiscountDescription(),
        leftProducts = leftProducts.toFSDiscountEyeSet(),
        rightProducts = rightProducts.toFSDiscountEyeSet(),
    )
}
