package com.peyess.salesapp.data.model.sale.purchase.discount

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.typing.products.DiscountCalcMethod

@Keep
@IgnoreExtraProperties
data class FSDiscountDescription(

    @Keep
    @JvmField
    @PropertyName("method")
    val method: String = DiscountCalcMethod.None.toName(),

    @Keep
    @JvmField
    @PropertyName("value")
    val value: Double = 0.0,
)