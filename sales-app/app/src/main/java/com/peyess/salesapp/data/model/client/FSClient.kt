package com.peyess.salesapp.data.model.client

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSClient(
    @JvmField
    @Keep
    @PropertyName("name")
    val name: String = "",

    @JvmField
    @Keep
    @PropertyName("name_display")
    val nameDisplay: String = "",

    @JvmField
    @Keep
    @PropertyName("birthday")
    val birthday: Timestamp = Timestamp.now(),

    @JvmField
    @Keep
    @PropertyName("document")
    val document: String = "",

    @JvmField
    @Keep
    @PropertyName("sex")
    val sex: String = "none",

    @JvmField
    @Keep
    @PropertyName("zip_code")
    val zipCode: String = "",

    @JvmField
    @Keep
    @PropertyName("street")
    val street: String = "",

    @JvmField
    @Keep
    @PropertyName("house_number")
    val houseNumber: String = "",

    @JvmField
    @Keep
    @PropertyName("complement")
    val complement: String = "",

    @JvmField
    @Keep
    @PropertyName("neighborhood")
    val neighborhood: String = "",

    @JvmField
    @Keep
    @PropertyName("city")
    val city: String = "",

    @JvmField
    @Keep
    @PropertyName("state")
    val state: String = "",

    @JvmField
    @Keep
    @PropertyName("email")
    val email: String = "",

    @JvmField
    @Keep
    @PropertyName("phone")
    val phone: String = "",

    @JvmField
    @Keep
    @PropertyName("cellphone")
    val cellphone: String = "",

    @JvmField
    @Keep
    @PropertyName("whatsapp")
    val whatsapp: String = "",

    @JvmField
    @Keep
    @PropertyName("stores_ids")
    val storesIds: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("doc_version")
    val doc_version: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val is_editable: Boolean = false,

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
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

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
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)

