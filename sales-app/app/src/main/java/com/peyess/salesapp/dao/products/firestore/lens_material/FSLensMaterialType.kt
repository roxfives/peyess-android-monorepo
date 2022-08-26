package com.peyess.salesapp.dao.products.firestore.lens_material

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensMaterialType(
    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",
)
