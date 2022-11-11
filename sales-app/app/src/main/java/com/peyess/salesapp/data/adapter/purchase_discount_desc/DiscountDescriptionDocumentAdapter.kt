package com.peyess.salesapp.data.adapter.purchase_discount_desc

import com.peyess.salesapp.data.adapter.payment_value_desc.toPaymentValueDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseProductsDiscount
import com.peyess.salesapp.data.model.sale.purchase.PurchaseProductsDiscountDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.FSDiscountDescription

fun DiscountDescriptionDocument.toFSDiscountDescription(): FSDiscountDescription {
    return FSDiscountDescription(
        method = method.toName(),
        value = value,
    )
}

fun FSPurchaseProductsDiscount.toPurchaseProductsDiscountDocument(): PurchaseProductsDiscountDocument {
    return PurchaseProductsDiscountDocument(
        lenses = lenses.map { it.toPaymentValueDescriptionDocument() },
        colorings = colorings.map { it.toPaymentValueDescriptionDocument() },
        treatments = treatments.map { it.toPaymentValueDescriptionDocument() },

        frames = frames.map  { it.toPaymentValueDescriptionDocument() },

        misc = misc.map { it.toPaymentValueDescriptionDocument() },
    )
}

