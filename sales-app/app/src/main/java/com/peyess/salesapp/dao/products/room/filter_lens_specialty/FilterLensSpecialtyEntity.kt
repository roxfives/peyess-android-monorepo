package com.peyess.salesapp.dao.products.room.filter_lens_specialty

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = FilterLensSpecialtyEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalLensEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["lens_id"],
//        )
//    ]
)
data class FilterLensSpecialtyEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
) {
    companion object {
        const val tableName = "filter_lens_specialty"
    }
}
