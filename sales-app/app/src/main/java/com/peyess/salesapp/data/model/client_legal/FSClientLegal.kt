package com.peyess.salesapp.data.model.client_legal

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSClientLegal(
    @JvmField
    @Keep
    @PropertyName("has_accepted_promotional_messages")
    val hasAcceptedPromotionalMessages: Boolean = false,

    @JvmField
    @Keep
    @PropertyName("method_promotional_messages")
    val methodPromotionalMessages: String = ClientLegalMethod.SalesAppCreateAccount.toName(),

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("created_allowed_by")
    val createdAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated_allowed_by")
    val updatedAllowedBy: String = "",
)
