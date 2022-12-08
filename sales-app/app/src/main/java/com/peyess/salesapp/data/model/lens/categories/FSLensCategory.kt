package com.peyess.salesapp.data.model.lens.categories

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensCategory(

    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("explanation")
    val explanation: String = "",

    @Keep
    @JvmField
    @PropertyName("is_mono")
    val isMono: Boolean = false,
)

fun FSLensCategory.toDocument(id: String) : LensTypeCategoryDocument {
    return LensTypeCategoryDocument(
        id = id,
        name = name,
        explanation = explanation,
        isMono = isMono,
    )
}
