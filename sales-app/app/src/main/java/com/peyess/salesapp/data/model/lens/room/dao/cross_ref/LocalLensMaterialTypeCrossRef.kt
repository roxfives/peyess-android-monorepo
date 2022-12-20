package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["material_id", "material_type_id"])
data class LocalLensMaterialTypeCrossRef(
    @ColumnInfo(name = "material_id")
    val materialId: String = "",

    @ColumnInfo(name = "material_type_id")
    val materialTypeId: String = "",
)
