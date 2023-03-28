package com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSDiscountEyeSet(
    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: FSDiscountDescription = FSDiscountDescription(),
)
