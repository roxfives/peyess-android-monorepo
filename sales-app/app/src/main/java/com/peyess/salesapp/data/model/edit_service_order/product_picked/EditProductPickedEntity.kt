package com.peyess.salesapp.data.model.edit_service_order.product_picked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity

@Entity(
    tableName = EditProductPickedEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditServiceOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["so_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class EditProductPickedEntity(
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
        const val tableName = "edit_product_picked"
    }
}
