package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.service_order.discount_description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSPaymentValueDescription(
    @Keep
    @JvmField
    @PropertyName("discount")
    val discount: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("price")
    val price: Double = 0.0,
)
