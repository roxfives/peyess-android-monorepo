package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldFramesDescription

@Keep
@IgnoreExtraProperties
data class FSDenormalizedServiceOrderDesc(
    @Keep
    @JvmField
    @PropertyName("has_own_frames")
    val hasOwnFrames: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("left_products")
    val leftProducts: FSProductSoldEyeSet = FSProductSoldEyeSet(),
    @Keep
    @JvmField
    @PropertyName("right_products")
    val rightProducts: FSProductSoldEyeSet = FSProductSoldEyeSet(),

    @Keep
    @JvmField
    @PropertyName("frames_products")
    val framesProducts: FSProductSoldFramesDescription = FSProductSoldFramesDescription(),

    @Keep
    @JvmField
    @PropertyName("misc")
    val miscProducts: List<FSProductSoldDescription> = emptyList(),
)
