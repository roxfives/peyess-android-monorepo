package com.peyess.salesapp.dao.products.room.filter_lens_family

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["lens_id"],
        )
    ]
)
data class FilterLensFamilyEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "name") val name: String = "",
) {
    companion object {
        const val tableName = "filter_lens_family"
    }
}
