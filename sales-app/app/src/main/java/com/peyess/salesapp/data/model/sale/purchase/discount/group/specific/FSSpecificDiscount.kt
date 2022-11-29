package com.peyess.salesapp.data.model.sale.purchase.discount.group.specific

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSSpecificDiscount(

    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: Map<String, FSDiscountDescription> = mapOf(),

    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: Map<String, FSDiscountDescription> = mapOf(),

    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: Map<String, FSDiscountDescription> = mapOf(),

    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: Map<String, FSDiscountDescription> = mapOf(),

    @Keep
    @JvmField
    @PropertyName("contacts")
    val contacts: Map<String, FSDiscountDescription> = mapOf(),

    @Keep
    @JvmField
    @PropertyName("others")
    val others: Map<String, FSDiscountDescription> = mapOf(),
)
