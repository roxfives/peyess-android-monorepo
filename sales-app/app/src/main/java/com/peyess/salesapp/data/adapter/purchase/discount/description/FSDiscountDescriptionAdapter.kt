package com.peyess.salesapp.data.adapter.purchase.discount.description

import com.peyess.salesapp.data.adapter.payment_value_desc.toFSPaymentValueDescription
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
        lenses = lenses.map { it.toFSPaymentValueDescription() },
        colorings = colorings.map { it.toFSPaymentValueDescription() },
        treatments = treatments.map { it.toFSPaymentValueDescription() },

        frames = frames.map  { it.toFSPaymentValueDescription() },

        misc = misc.map { it.toFSPaymentValueDescription() },
    )
}

