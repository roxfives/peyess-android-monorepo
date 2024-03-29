package com.peyess.salesapp.dao.products.room.filter_lens_category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = FilterLensCategoryEntity.tableName,
//    foreignKeys = [
//            ForeignKey(
//                entity = LocalLensEntity::class,
//                parentColumns = ["id"],
//                childColumns = ["lens_id"],
//            )
//    ]
)
data class FilterLensCategoryEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "name") val name: String = "",
) {
    companion object {
        const val tableName = "filter_lens_category"
    }
}
