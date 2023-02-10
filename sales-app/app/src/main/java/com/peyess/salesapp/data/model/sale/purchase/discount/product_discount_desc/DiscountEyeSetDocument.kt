package com.peyess.salesapp.data.model.sale.purchase.discount.product_discount_desc

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument

@Keep
@IgnoreExtraProperties
data class DiscountEyeSetDocument(
    @Keep
    @JvmField
    @PropertyName("lenses")
    val lenses: DiscountDescriptionDocument = DiscountDescriptionDocument(),

    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: DiscountDescriptionDocument = DiscountDescriptionDocument(),

    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: DiscountDescriptionDocument = DiscountDescriptionDocument(),
)
