package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensMaterialCategoryEntity.tableName)
data class LocalLensMaterialCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "store_priority")
    val storePriority: Int = 0,
) {
    companion object {
        const val tableName = "local_lens_material_category"
    }
}
