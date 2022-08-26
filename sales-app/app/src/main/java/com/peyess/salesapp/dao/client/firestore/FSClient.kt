package com.peyess.salesapp.dao.client.firestore

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.dao.products.firestore.disponibility.FSDispManufacturer

@Keep
@IgnoreExtraProperties
data class FSClient(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("name_display")
    val nameDisplay: String = "",

    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("sex")
    val sex: String = "",

    @Keep
    @JvmField
    @PropertyName("email")
    val email: String = "",

    @Keep
    @JvmField
    @PropertyName("document")
    val document: String = "",

    @Keep
    @JvmField
    @PropertyName("picture")
    val picture: String = "",

    @Keep
    @JvmField
    @PropertyName("short_address")
    val shortAddress: String = "",

    @Keep
    @JvmField
    @PropertyName("account_status")
    val accountStatus: String = "",
)

fun Client.toFirestore(): FSClient {
    return FSClient(
        id = id,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex,
        email = email,
        document = document,
        picture = picture,
        shortAddress = shortAddress,
        accountStatus = UserAccountStatus.fromType(accountStatus)!!,
    )
}
