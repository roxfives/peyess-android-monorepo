package com.peyess.salesapp.data.model.sale.service_order.products_sold

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription

@Keep
@IgnoreExtraProperties
data class FSProductsSold(
    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: Map<String, FSProductSoldDescription> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: Map<String, FSProductSoldDescription> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: Map<String, FSProductSoldDescription> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: FSProductSoldDescription = FSProductSoldDescription(),

    @Keep
    @JvmField
    @PropertyName("misc")
    val misc: Map<String, FSProductSoldDescription> = emptyMap(),
)