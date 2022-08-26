package com.peyess.salesapp.dao.client.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.client.firestore.Client

@Entity(
    tableName = ClientEntity.tableName,
    primaryKeys = ["so_id", "role"]
)
data class ClientEntity(
    @ColumnInfo(name = "id") val id: String = "",

    @ColumnInfo(name = "so_id")
    val soId: String = "",

    @ColumnInfo(name = "role")
    val clientRole: ClientRole = ClientRole.User,

    @ColumnInfo(name = "nameDisplay")
    val nameDisplay: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "sex")
    val sex: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "document")
    val document: String = "",

    @ColumnInfo(name = "picture")
    val picture: String = "",

    @ColumnInfo(name = "shortAddress")
    val shortAddress: String = "",
) {
    companion object {
        const val tableName = "sale_client_picked"
    }
}

fun Client.toEntity(soId: String, clientRole: ClientRole): ClientEntity {
    return ClientEntity(
        id = id,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex,
        email = email,
        document = document,
        picture = picture,
        shortAddress = shortAddress,

        soId = soId,
        clientRole = clientRole,
    )
}