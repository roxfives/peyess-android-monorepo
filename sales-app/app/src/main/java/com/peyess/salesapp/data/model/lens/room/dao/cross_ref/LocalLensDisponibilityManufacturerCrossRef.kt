package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["disp_id", "manufacturer_id"])
data class LocalLensDisponibilityManufacturerCrossRef(
    @ColumnInfo(name = "disp_id")
    val dispId: String = "",

    @ColumnInfo(name = "manufacturer_id")
    val manufacturerId: String = "",
)
