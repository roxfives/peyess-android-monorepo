package com.peyess.salesapp.data.model.lens.room.treatment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensTreatmentEntity.tableName)
data class LocalLensTreatmentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "brand")
    val brand: String = "",

    @ColumnInfo(name = "design")
    val design: String = "",

    @ColumnInfo(name = "is_coloring_required")
    val isColoringRequired: Boolean = false,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = false,

    @ColumnInfo(name = "is_local_enabled")
    val isLocalEnabled: Boolean = false,

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

    @ColumnInfo(name = "warning")
    val warning: String = "",
) {
    companion object {
        const val tableName = "local_lens_treatment"
    }
}
