package com.peyess.salesapp.data.model.sale.purchase.discount.group.standard

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSStandardDiscount(

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

    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("contacts")
    val contacts: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("others")
    val others: FSDiscountDescription = FSDiscountDescription(),
)
