package com.peyess.salesapp.data.model.local_sale.client_picked

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.peyess.salesapp.typing.sale.ClientRole

@Entity(
    tableName = ClientPickedEntity.tableName,
    primaryKeys = ["so_id", "role"]
)
data class ClientPickedEntity(
    @ColumnInfo(name = "id")
    val id: String = "",

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
