package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = LocalLensDetailsCrossRef.tableName,
    primaryKeys = [
        "brand_id",
        "design_id",
        "supplier_id",
        "group_id",
        "specialty_id",
        "tech_id",
        "type_id",
        "category_id",
        "material_id",
    ],
)
data class LocalLensDetailsCrossRef(
    @ColumnInfo(name = "brand_id", index = true)
    val brandId: String = "",
    @ColumnInfo(name = "design_id", index = true)
    val designId: String = "",
    @ColumnInfo(name = "supplier_id", index = true)
    val supplierId: String = "",
    @ColumnInfo(name = "group_id", index = true)
    val groupId: String = "",
    @ColumnInfo(name = "specialty_id", index = true)
    val specialtyId: String = "",
    @ColumnInfo(name = "tech_id", index = true)
    val techId: String = "",
    @ColumnInfo(name = "type_id", index = true)
    val typeId: String = "",
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: String = "",
    @ColumnInfo(name = "material_id", index = true)
    val materialId: String = "",
) {
    companion object {
        const val tableName = "local_lenses_details_cross_ref"
    }
}
