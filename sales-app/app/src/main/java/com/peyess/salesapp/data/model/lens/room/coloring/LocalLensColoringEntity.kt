package com.peyess.salesapp.data.model.lens.room.coloring

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensColoringEntity.tableName)
data class LocalLensColoringEntity(
    @PrimaryKey
    @ColumnInfo(name = "id", index = true)
    val id: String = "",

    @ColumnInfo(name = "brand")
    val brand: String = "",

    @ColumnInfo(name = "design")
    val design: String = "",

    @ColumnInfo(name = "has_medical")
    val hasMedical: Boolean = false,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = false,

    @ColumnInfo(name = "is_local_enabled")
    val isLocalEnabled: Boolean = false,

    @ColumnInfo(name = "is_treatment_required")
    val isTreatmentRequired: Boolean = false,

    @ColumnInfo(name = "observation")
    val observation: String = "",

    @ColumnInfo(name = "priority")
    val priority: Double = 0.0,

    @ColumnInfo(name = "shipping_time")
    val shippingTime: Double = 0.0,

    @ColumnInfo(name = "specialty")
    val specialty: String = "",

    @ColumnInfo(name = "supplier_id")
    val supplierId: String = "",

    @ColumnInfo(name = "supplier")
    val supplier: String = "",

    @ColumnInfo(name = "type")
    val type: String = "",

    @ColumnInfo(name = "warning")
    val warning: String = "",
) {
    companion object {
        const val tableName = "local_lens_coloring"
    }
}
