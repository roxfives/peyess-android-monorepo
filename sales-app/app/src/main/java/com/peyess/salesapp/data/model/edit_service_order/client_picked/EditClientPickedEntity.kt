package com.peyess.salesapp.data.model.edit_service_order.client_picked

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.typing.sale.ClientRole

@Entity(
    tableName = EditClientPickedEntity.tableName,
    primaryKeys = ["so_id", "role"]
)
data class EditClientPickedEntity(
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
    val sex: Sex = Sex.Unknown,

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "document")
    val document: String = "",

    @ColumnInfo(name = "short_address")
    val shortAddress: String = "",
) {
    companion object {
        const val tableName = "edit_sale_client_picked"
    }
}
