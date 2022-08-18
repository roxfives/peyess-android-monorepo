package com.peyess.salesapp.dao.products.firestore.lens_alt_height

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

data class FSLensAltHeight(
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