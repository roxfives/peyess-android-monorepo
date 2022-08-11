package com.peyess.salesapp.dao.products.room.local_treatment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["lens_id"],
            onDelete = CASCADE,
        )
    ]
)
data class LocalTreatmentEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "lens_id")
    val lensId: String = "",

    @ColumnInfo(name = "specialty")
    val specialty: String = "",

    @ColumnInfo(name = "is_coloring_required")
    val isColoringRequired: Boolean = false,

    @ColumnInfo(name = "priority")
    val priority: Double = 0.0,

    @ColumnInfo(name = "is_generic")
    val isGeneric: Boolean = false,

    @ColumnInfo(name = "cost")
    val cost: Double = 0.0,
    @ColumnInfo(name = "price")
    val price: Double = 0.0,
    @ColumnInfo(name = "suggested_price")
    val suggestedPrice: Double = 0.0,

    @ColumnInfo(name = "shipping_time")
    val shippingTime: Double = 0.0,

    @ColumnInfo(name = "observation")
    val observation: String = "",
    @ColumnInfo(name = "warning")
    val warning: String = "",

    @ColumnInfo(name = "brand")
    val brand: String = "",
    @ColumnInfo(name = "brand_id")
    val brandId: String = "",
    @ColumnInfo(name = "design")
    val design: String = "",
    @ColumnInfo(name = "design_id")
    val designId: String = "",

    @ColumnInfo(name = "supplier_picture")
    val supplierPicture: String = "",
    @ColumnInfo(name = "supplier")
    val supplier: String = "",
    @ColumnInfo(name = "supplier_id")
    val supplierId: String = "",

    @ColumnInfo(name = "is_manufacturing_local")
    val isManufacturingLocal: Boolean = false,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = false,
    @ColumnInfo(name = "reason_disabled")
    val reasonDisabled: String = "",

//    @ColumnInfo(name = "release_date")
//    val releaseDate: Date = Date(),
) {
    companion object {
        const val tableName = "local_treatment"
    }
}
