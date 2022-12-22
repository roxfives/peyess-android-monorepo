package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = LocalLensDisponibilityManufacturerCrossRef.tableName,
    primaryKeys = ["disp_id", "manufacturer_id"],
)
data class LocalLensDisponibilityManufacturerCrossRef(
    @ColumnInfo(name = "disp_id")
    val dispId: Long = 0L,

    @ColumnInfo(name = "manufacturer_id")
    val manufacturerId: String = "",
) {
    companion object {
        const val tableName = "local_lenses_disponibility_manufacturer_cross_ref"
    }
}
