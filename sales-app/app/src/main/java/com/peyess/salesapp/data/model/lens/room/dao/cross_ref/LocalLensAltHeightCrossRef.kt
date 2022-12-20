package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["lens_id", "alt_height_id"])
data class LocalLensAltHeightCrossRef(
    @ColumnInfo(name = "lens_id")
    val lensId: String = "",

    @ColumnInfo(name = "alt_height_id")
    val altHeightId: String = "",
)
