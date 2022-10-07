package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSDenormalizedServiceOrderDesc(

    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: List<FSDenormalizedPurchaseDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: List<FSDenormalizedPurchaseDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: FSDenormalizedPurchaseDescription = FSDenormalizedPurchaseDescription(),

    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: List<FSDenormalizedPurchaseDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("misc")
    val misc: List<FSDenormalizedPurchaseDescription> = emptyList(),
)
