package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSDenormalizedPurchaseDescription(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("units")
    val units: Int = 0,

    @Keep
    @JvmField
    @PropertyName("description")
    val description: String = "",
)