package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensEntity

@Entity(
    tableName = LocalLensColoringCrossRef.tableName,
    primaryKeys = ["lens_id", "coloring_id"],
)
data class LocalLensColoringCrossRef(
    @ColumnInfo(name = "lens_id")
    val lensId: String = "",

    @ColumnInfo(name = "coloring_id")
    val coloringId: String = "",

    @ColumnInfo(name = "price")
    val price: Double = 0.0,
) {
    companion object {
        const val tableName = "local_lenses_coloring_cross_ref"
    }
}
