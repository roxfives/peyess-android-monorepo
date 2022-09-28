package com.peyess.salesapp.dao.client.firestore

import android.net.Uri

data class ClientDocument(
    val id: String = "",
    val nameDisplay: String = "",
    val name: String = "",
    val sex: String = "",
    val email: String = "",
    val document: String = "",
    val picture: Uri = Uri.EMPTY,
    val shortAddress: String = "",
    val accountStatus: UserAccountStatus = UserAccountStatus.Activated,
)

fun FSClient.toDocument(id: String): ClientDocument {
    return ClientDocument(
        id = id,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex,
        email = email,
        document = document,
        picture = Uri.parse(picture),
        shortAddress = street,
    )
}
