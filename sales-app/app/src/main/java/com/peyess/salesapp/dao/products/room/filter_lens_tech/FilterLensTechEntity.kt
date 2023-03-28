package com.peyess.salesapp.dao.products.room.filter_lens_tech

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = FilterLensTechEntity.tableName,
)
data class FilterLensTechEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
) {
    companion object {
        const val tableName = "filter_lens_tech"
    }
}
