package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensTypeCategoryEntity.tableName)
data class LocalLensTypeCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "explanation")
    val explanation: String = "",
) {
    companion object {
        const val tableName = "local_lens_type_category"
    }
}
