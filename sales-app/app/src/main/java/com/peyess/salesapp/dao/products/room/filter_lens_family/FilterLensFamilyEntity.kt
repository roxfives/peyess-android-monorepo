package com.peyess.salesapp.dao.products.room.filter_lens_family

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = FilterLensFamilyEntity.tableName,
)
data class FilterLensFamilyEntity(
    @PrimaryKey(autoGenerate = true) val primaryKey: Int = 0,
    @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "supplier_id") val supplierId: String = "",
    @ColumnInfo(name = "priority") val priority: Double = 0.0,
) {
    companion object {
        const val tableName = "filter_lens_family"
    }
}
