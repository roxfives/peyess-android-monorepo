package com.peyess.salesapp.dao.products.room.join_lens_coloring

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = JoinLensColoringEntity.tableName,
    primaryKeys = ["lens_id",  "coloring_id"],
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["lens_id"],
        ),
        ForeignKey(
            entity = LocalColoringEntity::class,
            parentColumns = ["id"],
            childColumns = ["coloring_id"],
        ),
    ]
)
data class JoinLensColoringEntity(
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "coloring_id") val coloringId: String = "",
) {
    companion object {
        const val tableName = "join_lens_coloring"
    }
}
