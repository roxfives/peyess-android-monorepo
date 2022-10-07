package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSPurchaseProductsDiscount(
    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: List<FSPaymentValueDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: List<FSPaymentValueDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: List<FSPaymentValueDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("frames")
    val frames: List<FSPaymentValueDescription> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("misc")
    val misc: List<FSPaymentValueDescription> = emptyList(),

)
