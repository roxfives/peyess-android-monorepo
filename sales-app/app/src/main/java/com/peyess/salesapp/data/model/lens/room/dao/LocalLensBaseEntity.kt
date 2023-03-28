package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalLensBaseEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["lens_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
    ]
)
data class LocalLensBaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "lens_id", index = true)
    val lens_id: String = "",

    @ColumnInfo(name = "base")
    val base: Double = 0.0,
) {
    companion object {
        const val tableName = "local_lenses_bases"
    }
}
