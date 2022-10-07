package com.peyess.salesapp.data.model.sale.purchase

data class PurchaseProductsDiscountDocument(
    val lenses: List<PaymentValueDescriptionDocument> = emptyList(),

    val colorings: List<PaymentValueDescriptionDocument> = emptyList(),

    val treatments: List<PaymentValueDescriptionDocument> = emptyList(),

    val frames: List<PaymentValueDescriptionDocument> = emptyList(),

    val misc: List<PaymentValueDescriptionDocument> = emptyList(),

)
