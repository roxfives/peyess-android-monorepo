package com.peyess.salesapp.dao.products.room.join_lens_material

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = JoinLensMaterialEntity.tableName,
    primaryKeys = ["lens_id",  "material_id"],
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalLensEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["lens_id"],
//        ),
//        ForeignKey(
//            entity = FilterLensMaterialEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["material_id"],
//        ),
//    ]
)
data class JoinLensMaterialEntity(
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "material_id") val materialId: String = "",
) {
    companion object {
        const val tableName = "join_lens_material"
    }
}
