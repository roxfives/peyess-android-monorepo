package com.peyess.salesapp.dao.sale.active_sale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ActiveSalesEntity.tableName)
data class ActiveSalesEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "collaborator_uid")
    val collaboratorUid: String = "",
    @ColumnInfo(name = "client_name")
    val clientName: String = "",
    @ColumnInfo(name = "active")
    val active: Boolean = false,
    @ColumnInfo(name = "is_uploading")
    val isUploading: Boolean = false,
) {
    companion object {
        const val tableName = "active_sales"
    }
}
