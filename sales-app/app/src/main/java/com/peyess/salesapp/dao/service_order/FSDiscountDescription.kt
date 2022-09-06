package com.peyess.salesapp.dao.service_order

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FSDiscountDescription(
    @Keep
    @JvmField
    @PropertyName("method")
    val method: String = "",
    @Keep
    @JvmField
    @PropertyName("value")
    val value: Double = 0.0,
)
