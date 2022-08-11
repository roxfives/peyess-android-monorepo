package com.peyess.salesapp.dao.products.firestore.disponibility

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

data class FSDispManufacturer(
    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("shipping_time")
    val shippingTime: Double = 0.0,
)
