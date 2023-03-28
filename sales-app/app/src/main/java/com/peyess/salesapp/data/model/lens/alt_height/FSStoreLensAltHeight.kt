package com.peyess.salesapp.data.model.lens.alt_height

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSStoreLensAltHeight(
    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("name_display")
    val name_display: String = "",

    @Keep
    @JvmField
    @PropertyName("value")
    val value: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("observation")
    val observation: String = "",
)
