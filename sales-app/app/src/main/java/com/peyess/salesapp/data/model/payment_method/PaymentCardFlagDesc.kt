package com.peyess.salesapp.data.model.payment_method

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class PaymentCardFlagDesc(
    @Keep
    @PropertyName("name")
    val name: String = "",

    @Keep
    @PropertyName("icon")
    val icon: String = "",

    @Keep
    @PropertyName("is_accepted")
    val isAccepted: Boolean = true,
)
