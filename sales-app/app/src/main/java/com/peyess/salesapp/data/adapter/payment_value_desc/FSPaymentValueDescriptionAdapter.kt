package com.peyess.salesapp.data.adapter.payment_value_desc

import com.peyess.salesapp.data.adapter.purchase_discount_desc.toDiscountDescriptionDocument
import com.peyess.salesapp.data.adapter.purchase_discount_desc.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.FSPaymentValueDescription
import com.peyess.salesapp.data.model.sale.purchase.PaymentValueDescriptionDocument

fun FSPaymentValueDescription.toPaymentValueDescriptionDocument(): PaymentValueDescriptionDocument {
    return PaymentValueDescriptionDocument(
        discount = discount.toDiscountDescriptionDocument(),
        price = price,
    )
}