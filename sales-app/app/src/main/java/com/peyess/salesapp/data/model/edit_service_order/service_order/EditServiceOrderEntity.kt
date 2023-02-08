package com.peyess.salesapp.data.model.edit_service_order.service_order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import com.peyess.salesapp.typing.lens.LensTypeCategoryName

@Entity(
    tableName = EditServiceOrderEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditSaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"],
            onDelete = CASCADE,
         ),
    ],
)
data class EditServiceOrderEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "has_prescription")
    val hasPrescription: Boolean = true,
    @ColumnInfo(name = "sale_id", index = true)
    val saleId: String = "",

    @ColumnInfo(name = "client_name")
    val clientName: String = "",
    @ColumnInfo(name = "lens_type_name")
    val lensTypeCategoryName: LensTypeCategoryName = LensTypeCategoryName.None,
    @ColumnInfo(name = "is_lens_type_mono")
    val isLensTypeMono: Boolean = false,
) {
    companion object {
        const val tableName = "edit_service_order"
    }
}
