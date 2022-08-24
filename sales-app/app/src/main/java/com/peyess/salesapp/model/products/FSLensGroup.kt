package com.peyess.salesapp.model.products

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensGroup(

    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: Double = 0.0,
)

fun FSLensGroup.toDocument(id: String) : LensGroup {
    return LensGroup(
        id = id,
        name = name,
        priority = priority,
    )
}
