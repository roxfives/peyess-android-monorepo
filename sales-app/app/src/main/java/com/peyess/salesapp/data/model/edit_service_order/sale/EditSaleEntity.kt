package com.peyess.salesapp.data.model.edit_service_order.sale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EditSaleEntity.tableName)
data class EditSaleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "collaborator_uid")
    val collaboratorUid: String = "",
    @ColumnInfo(name = "active")
    val active: Boolean = false,
    @ColumnInfo(name = "is_uploading")
    val isUploading: Boolean = false,
) {
    companion object {
        const val tableName = "edit_sale"
    }
}
