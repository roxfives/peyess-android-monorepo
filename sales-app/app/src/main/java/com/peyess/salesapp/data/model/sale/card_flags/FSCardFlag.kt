package com.peyess.salesapp.data.model.sale.card_flags

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSCardFlag(
    @JvmField
    @Keep
    @PropertyName("name")
    val name: String = "",
    @JvmField
    @Keep
    @PropertyName("icon")
    val icon: String = "",

    @JvmField
    @Keep
    @PropertyName("doc_version")
    val docVersion: Int = 0,

    @JvmField
    @Keep
    @PropertyName("is_editable")
    val isEditable: Boolean = false,

    @JvmField
    @Keep
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),
    @JvmField
    @Keep
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),

    @JvmField
    @Keep
    @PropertyName("created_by")
    val createdBy: String = "",
    @JvmField
    @Keep
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @JvmField
    @Keep
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",
    @JvmField
    @Keep
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)
