package com.peyess.salesapp.dao.sale.lens_comparison

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity

@Entity(
    tableName = LensComparisonEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = ActiveSOEntity::class,
            parentColumns = ["id"],
            childColumns = ["so_id"],
        ),
    ]
)
data class LensComparisonEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "so_id") val soId: String = "",
    // TODO: Add as foreign key
    @ColumnInfo(name = "original_lens_id") val originalLensId: String = "",
    @ColumnInfo(name = "comparison_lens_id") val comparisonLensId: String = "",
) {
    companion object {
        const val tableName = "lens_comparison"
    }
}
