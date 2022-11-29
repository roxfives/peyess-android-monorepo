package com.peyess.salesapp.data.model.sale.purchase.discount.set

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSDiscountSet(
    @Keep
    @JvmField
    @PropertyName("query")
    val query: Map<String, String> = mapOf(),

    @Keep
    @JvmField
    @PropertyName("discount")
    val discount: FSDiscountDescription = FSDiscountDescription(),
)
