package com.peyess.salesapp.dao.products.firestore.lens_description

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensDescription(
    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("families_ids")
    val familiesIds: List<String> = emptyList(),
)

fun FSLensDescription.toDocument(id: String): LensDescription {
    return LensDescription(
        id = id,
        name = name,
        priority = priority,
        familiesIds = familiesIds,
    )
}