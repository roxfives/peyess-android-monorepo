package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FSDenormalizedClient(
    @Keep
    @JvmField
    @PropertyName("uid")
    val uid: String = "",

    @Keep
    @JvmField
    @PropertyName("document")
    val document: String = "",

    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("picture")
    val picture: String = "",
)
