package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensMaterialTypeEntity.tableName)
data class LocalLensMaterialTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",
) {
    companion object {
        const val tableName = "local_lenses_material_types"
    }
}
