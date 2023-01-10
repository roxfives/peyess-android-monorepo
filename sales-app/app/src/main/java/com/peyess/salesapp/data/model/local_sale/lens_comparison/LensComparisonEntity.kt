package com.peyess.salesapp.data.model.local_sale.lens_comparison

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = LensComparisonEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//        ),
//    ]
)
data class LensComparisonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "so_id")
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
        const val tableName = "lens_comparison"
    }
}
