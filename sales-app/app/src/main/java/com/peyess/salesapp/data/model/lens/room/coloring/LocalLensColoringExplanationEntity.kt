package com.peyess.salesapp.data.model.lens.room.coloring

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalLensColoringExplanationEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalLensColoringEntity::class,
            parentColumns = ["id"],
            childColumns = ["coloring_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        )
    ]
)
data class LocalLensColoringExplanationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "coloring_id", index = true)
    val coloringId: String = "",

    @ColumnInfo(name = "explanation")
    val explanation: String = "",
) {
    companion object {
        const val tableName = "local_lens_coloring_exp"
    }
}
