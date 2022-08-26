package com.peyess.salesapp.dao.products.firestore.lens_family

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensFamily(
    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: String = "",
)

fun FSLensFamily.toDocument(id: String): LensFamily {
    return LensFamily(
        id = id,
        name = name,
        priority = priority,
    )
}
