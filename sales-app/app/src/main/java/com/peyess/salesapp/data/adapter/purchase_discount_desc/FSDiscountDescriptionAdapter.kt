package com.peyess.salesapp.data.adapter.purchase_discount_desc

import com.peyess.salesapp.data.adapter.payment_value_desc.toFSPaymentValueDescription
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseProductsDiscount
import com.peyess.salesapp.data.model.sale.purchase.PurchaseProductsDiscountDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.FSDiscountDescription
import com.peyess.salesapp.typing.products.DiscountCalcMethod

fun FSDiscountDescription.toDiscountDescriptionDocument(): DiscountDescriptionDocument {
    return DiscountDescriptionDocument(
        method = DiscountCalcMethod.fromName(method),
        value = value,
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

