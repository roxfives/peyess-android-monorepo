package com.peyess.salesapp.dao.products.room.filter_lens_material

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = FilterLensMaterialEntity.tableName,
)
data class FilterLensMaterialEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "supplier_id") val supplierId: String = "",
    @ColumnInfo(name = "name") val name: String = "",
) {
    companion object {
        const val tableName = "filter_lens_material"
    }
}
