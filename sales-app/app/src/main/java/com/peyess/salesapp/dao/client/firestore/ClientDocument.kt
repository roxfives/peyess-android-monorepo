package com.peyess.salesapp.dao.client.firestore

data class ClientDocument(
    val id: String = "",
    val nameDisplay: String = "",
    val name: String = "",
    val sex: String = "",
    val email: String = "",
    val document: String = "",
    val picture: String = "",
    val shortAddress: String = "",
    val accountStatus: UserAccountStatus = UserAccountStatus.Activated,
)

fun FSClient.toDocument(): ClientDocument {
    return ClientDocument(
        id = id,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex,
        email = email,
        document = document,
        picture = picture,
        shortAddress = shortAddress,
        accountStatus = UserAccountStatus.fromName(accountStatus) ?: UserAccountStatus.Deactivated,
    )
}
