package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["lens_type_id", "category_id"],
    tableName = LocalLensTypeCategoryCrossRef.tableName,
)
data class LocalLensTypeCategoryCrossRef(
    @ColumnInfo(name = "lens_type_id")
    val lensTypeId: String = "",

    @ColumnInfo(name = "category_id")
    val categoryId: String = "",
) {
    companion object {
        const val tableName = "local_lenses_type_category_cross_ref"
    }
}
