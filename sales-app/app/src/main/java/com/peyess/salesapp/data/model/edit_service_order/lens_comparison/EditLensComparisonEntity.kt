package com.peyess.salesapp.data.model.edit_service_order.lens_comparison

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity

@Entity(
    tableName = EditLensComparisonEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditServiceOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["so_id"],
            onDelete = CASCADE,
        ),
    ],
)
data class EditLensComparisonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "so_id", index = true)
    val soId: String = "",

    @ColumnInfo(name = "original_lens_id")
    val originalLensId: String = "",
    @ColumnInfo(name = "original_coloring_id")
    val originalColoringId: String = "",
    @ColumnInfo(name = "original_treatment_id")
    val originalTreatmentId: String = "",

    @ColumnInfo(name = "comparison_lens_id")
    val comparisonLensId: String = "",
    @ColumnInfo(name = "comparison_coloring_id")
    val comparisonColoringId: String = "",
    @ColumnInfo(name = "comparison_treatment_id")
    val comparisonTreatmentId: String = "",
) {
    companion object {
        const val tableName = "edit_lens_comparison"
    }
}
