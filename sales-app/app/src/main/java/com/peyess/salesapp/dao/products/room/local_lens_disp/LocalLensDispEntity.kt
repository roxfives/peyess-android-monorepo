package com.peyess.salesapp.dao.products.room.local_lens_disp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = LocalLensDispEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["lens_id"],
        )
    ]
)
data class LocalLensDispEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "lens_id") val lensId: String = "",

    @ColumnInfo(name = "diam") val diam: Double = 0.0,
    @ColumnInfo(name = "max_cyl") val maxCyl: Double = 0.0,
    @ColumnInfo(name = "min_cyl") val minCyl: Double = 0.0,
    @ColumnInfo(name = "max_sph") val maxSph: Double = 0.0,
    @ColumnInfo(name = "min_sph") val minSph: Double = 0.0,
    @ColumnInfo(name = "max_add") val maxAdd: Double = 0.0,
    @ColumnInfo(name = "min_add") val minAdd: Double = 0.0,

    @ColumnInfo(name = "has_prism") val hasPrism: Boolean = false,
    @ColumnInfo(name = "prism") val prism: Double = 0.0,
    @ColumnInfo(name = "prism_price") val prismPrice: Double = 0.0,
    @ColumnInfo(name = "prism_cost") val prismCost: Double = 0.0,
    @ColumnInfo(name = "separate_prism") val separatePrism: Boolean = false,

    @ColumnInfo(name = "needs_check") val needsCheck: Boolean = false,
    @ColumnInfo(name = "sum_rule") val sumRule: Boolean = false,
) {
    companion object {
        const val tableName = "local_disp"
    }
}
