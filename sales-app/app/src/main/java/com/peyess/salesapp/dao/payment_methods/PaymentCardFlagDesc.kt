package com.peyess.salesapp.dao.payment_methods

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
