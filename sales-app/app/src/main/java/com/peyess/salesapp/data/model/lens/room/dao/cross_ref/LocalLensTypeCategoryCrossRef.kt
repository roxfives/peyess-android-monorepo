package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["lens_type_id", "category_id"])
data class LocalLensTypeCategoryCrossRef(
    @ColumnInfo(name = "lens_type_id")
    val lensTypeId: String = "",

    @ColumnInfo(name = "category_id")
    val categoryId: String = "",
)
