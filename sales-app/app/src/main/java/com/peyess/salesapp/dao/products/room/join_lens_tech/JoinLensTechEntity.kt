package com.peyess.salesapp.dao.products.room.join_lens_tech

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = JoinLensTechEntity.tableName,
    primaryKeys = ["lens_id",  "tech_id"],
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalLensEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["lens_id"],
//        ),
//        ForeignKey(
//            entity = FilterLensTechEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["tech_id"],
//        ),
//    ]
)
data class JoinLensTechEntity(
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "tech_id") val techId: String = "",
) {
    companion object {
        const val tableName = "join_lens_tech"
    }
}
