package com.peyess.salesapp.data.adapter.payment_value_desc

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.FSPaymentValueDescription
import com.peyess.salesapp.data.model.sale.purchase.PaymentValueDescriptionDocument

fun PaymentValueDescriptionDocument.toFSPaymentValueDescription(): FSPaymentValueDescription {
    return FSPaymentValueDescription(
        discount = discount.toFSDiscountDescription(),
        price = price,
    )
}