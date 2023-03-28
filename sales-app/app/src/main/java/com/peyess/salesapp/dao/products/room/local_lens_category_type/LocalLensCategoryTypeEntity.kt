package com.peyess.salesapp.dao.products.room.local_lens_category_type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = LocalLensCategoryTypeEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalLensEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["lens_id"],
//        )
//    ]
)
data class LocalLensCategoryTypeEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "name") val name: String = "",
) {
    companion object {
        const val tableName = "local_category_type"
    }
}
