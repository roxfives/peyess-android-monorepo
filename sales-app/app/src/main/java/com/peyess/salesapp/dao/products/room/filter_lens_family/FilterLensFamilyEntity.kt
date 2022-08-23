package com.peyess.salesapp.dao.products.room.filter_lens_family

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = FilterLensFamilyEntity.tableName,
)
data class FilterLensFamilyEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "supplier_id") val supplierId: String = "",
    @ColumnInfo(name = "priority") val priority: Double = 0.0,
) {
    companion object {
        const val tableName = "filter_lens_family"
    }
}
