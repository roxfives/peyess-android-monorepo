package com.peyess.salesapp.dao.service_order

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FSProductsSold(
    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: Map<String, FSSoldProductDescription> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: Map<String, FSSoldProductDescription> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: Map<String, FSSoldProductDescription> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: FSSoldProductDescription = FSSoldProductDescription(),

    @Keep
    @JvmField
    @PropertyName("misc")
    val misc: List<FSSoldProductDescription> = emptyList(),
)
