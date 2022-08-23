package com.peyess.salesapp.dao.products.room.filter_lens_description

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = FilterLensDescriptionEntity.tableName
)
data class FilterLensDescriptionEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "priority") val priority: Double = 0.0,
    @ColumnInfo(name = "family_id") val familyId: String = "",
) {
    companion object {
        const val tableName = "filter_lens_description"
    }
}
