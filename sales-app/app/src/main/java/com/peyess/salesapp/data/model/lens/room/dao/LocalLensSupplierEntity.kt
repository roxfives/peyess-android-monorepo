package com.peyess.salesapp.data.model.lens.room.dao

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensSupplierEntity.tableName)
data class LocalLensSupplierEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "picture")
    val picture: Uri = Uri.EMPTY,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "store_priority")
    val storePriority: Int = 0,
) {
    companion object {
        const val tableName = "local_lens_supplier"
    }
}
