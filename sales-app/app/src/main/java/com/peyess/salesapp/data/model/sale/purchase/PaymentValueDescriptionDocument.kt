package com.peyess.salesapp.data.model.sale.purchase

import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument

data class PaymentValueDescriptionDocument(
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),

    val price: Double = 0.0,
)
