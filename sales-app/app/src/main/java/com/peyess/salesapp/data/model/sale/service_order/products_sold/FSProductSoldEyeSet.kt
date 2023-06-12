package com.peyess.salesapp.data.model.sale.service_order.products_sold

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSLensSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription

@Keep
@IgnoreExtraProperties
data class FSProductSoldEyeSet(
    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: FSLensSoldDescription = FSLensSoldDescription(),

    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: FSProductSoldDescription = FSProductSoldDescription(),

    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: FSProductSoldDescription = FSProductSoldDescription(),
)
