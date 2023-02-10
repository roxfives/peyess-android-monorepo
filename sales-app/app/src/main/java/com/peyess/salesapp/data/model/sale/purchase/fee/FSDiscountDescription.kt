package com.peyess.salesapp.data.model.sale.purchase.fee

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

@Keep
@IgnoreExtraProperties
data class FSFeeDescription(
    @Keep
    @JvmField
    @PropertyName("method")
    val method: String = PaymentFeeCalcMethod.None.toName(),

    @Keep
    @JvmField
    @PropertyName("value")
    val value: Double = 0.0,
)
