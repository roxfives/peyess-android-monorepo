package com.peyess.salesapp.dao.products.room.local_lens_base

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = LocalLensBaseEntity.tableName,
//    foreignKeys = [ForeignKey(
//        entity = LocalLensEntity::class,
//        parentColumns = ["id"],
//        childColumns = ["lens_id"],
//        onDelete = CASCADE,
//    )]
)
data class LocalLensBaseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "lens_id") val lens_id: String = "",
    @ColumnInfo(name = "base") val base: Double = 0.0,
) {
    companion object {
        const val tableName = "lens_base"
    }
}
