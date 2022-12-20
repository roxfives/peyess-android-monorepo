package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["lens_id", "coloring_id"])
data class LocalLensColoringCrossRef(
    @ColumnInfo(name = "lens_id")
    val lensId: String = "",

    @ColumnInfo(name = "coloring_id")
    val coloringId: String = "",
) {
    companion object {
        const val tableName = "local_lenses_coloring_cross_ref"
    }
}
