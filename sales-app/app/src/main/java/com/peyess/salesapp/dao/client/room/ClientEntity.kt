package com.peyess.salesapp.dao.client.room

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.peyess.salesapp.dao.client.firestore.ClientDocument

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

    @ColumnInfo(name = "name_display")
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
    val picture: Uri = Uri.EMPTY,

    @ColumnInfo(name = "short_address")
    val shortAddress: String = "",
) {
    companion object {
        const val tableName = "sale_client_picked"
    }
}

fun ClientDocument.toEntity(soId: String, clientRole: ClientRole): ClientEntity {
    return ClientEntity(
        id = id,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex.toName(),
        email = email,
        document = document,
        picture = picture,
        shortAddress = shortAddress,

        soId = soId,
        clientRole = clientRole,
    )
}