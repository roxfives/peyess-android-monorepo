package com.peyess.salesapp.dao.sale.product_picked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = ProductPickedEntity.tableName,
)
data class ProductPickedEntity(
    @PrimaryKey
    @ColumnInfo(name = "so_id")
    val soId: String = "",

    @ColumnInfo(name = "lens_id")
    val lensId: String = "",
    @ColumnInfo(name = "treatment_id")
    val treatmentId: String = "",
    @ColumnInfo(name = "coloring_id")
    val coloringId: String = "",
) {
    companion object {
        const val tableName = "product_picked"
    }
}
