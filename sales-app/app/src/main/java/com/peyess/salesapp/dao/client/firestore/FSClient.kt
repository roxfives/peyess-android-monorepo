package com.peyess.salesapp.dao.client.firestore

import android.net.Uri
import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.*

@Keep
@IgnoreExtraProperties
data class FSClient(
//    @JvmField
//    @Keep
//    @PropertyName("id")
//    val id: String = "",

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
    @PropertyName("picture")
    val picture: String = "",

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
    @PropertyName("created_at")
    val createdAt: Timestamp = Timestamp.now(),

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
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated_allowed_by")
    val updatedAllowedBy: String = "",
)

fun ClientDocument.toFirestore(): FSClient {
    return FSClient(
        nameDisplay = nameDisplay,
        name = name,
        sex = sex,
        email = email,
        document = document,
//        picture = picture,
//        shortAddress = shortAddress,
//        accountStatus = UserAccountStatus.fromType(accountStatus)!!,
    )
}
