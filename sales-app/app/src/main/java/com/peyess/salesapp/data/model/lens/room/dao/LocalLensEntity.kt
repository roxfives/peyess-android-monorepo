package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(
    tableName = LocalLensEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalLensFamilyEntity::class,
            parentColumns = ["id"],
            childColumns = ["brand_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensDescriptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["design_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensSupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplier_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensSpecialtyEntity::class,
            parentColumns = ["id"],
            childColumns = ["specialty_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensTechEntity::class,
            parentColumns = ["id"],
            childColumns = ["tech_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["type_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
        ForeignKey(
            entity = LocalLensMaterialEntity::class,
            parentColumns = ["id"],
            childColumns = ["material_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        ),
//        ForeignKey(
//            entity = LocalLensTreatmentEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["default_treatment_id"],
//            onDelete = NO_ACTION,
//            onUpdate = NO_ACTION,
//        ),
    ]
)
data class LocalLensEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "priority")
    val priority: Double = 0.0,
    @ColumnInfo(name = "store_lens_priority")
    val storeLensPriority: Int = 0,

    @ColumnInfo(name = "height")
    val height: Double = 0.0,

    @ColumnInfo(name = "needs_check")
    val needsCheck: Boolean = false,
    @ColumnInfo(name = "has_addition")
    val hasAddition: Boolean = false,
    @ColumnInfo(name = "has_filter_blue")
    val hasFilterBlue: Boolean = false,
    @ColumnInfo(name = "has_filter_uv")
    val hasFilterUv: Boolean = false,
    @ColumnInfo(name = "is_coloring_treatment_mutex")
    val isColoringTreatmentMutex: Boolean = false,
    @ColumnInfo(name = "is_coloring_discounted")
    val isColoringDiscounted: Boolean = false,
    @ColumnInfo(name = "is_coloring_included")
    val isColoringIncluded: Boolean = false,
    @ColumnInfo(name = "is_treatment_discounted")
    val isTreatmentDiscounted: Boolean = false,
    @ColumnInfo(name = "is_treatment_included")
    val isTreatmentIncluded: Boolean = false,
    @ColumnInfo(name = "is_generic")
    val isGeneric: Boolean = false,

    @ColumnInfo(name = "shipping_time")
    val shippingTime: Double = 0.0,

    @ColumnInfo(name = "observation")
    val observation: String = "",
    @ColumnInfo(name = "warning")
    val warning: String = "",

    @ColumnInfo(name = "is_manufacturing_local")
    val isManufacturingLocal: Boolean = false,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = false,
    @ColumnInfo(name = "reason_disabled")
    val reasonDisabled: String = "",
    @ColumnInfo(name = "is_local_enabled")
    val isLocalEnabled: Boolean = true,
    @ColumnInfo(name = "reason_local_disabled")
    val reasonLocalDisabled: String = "",

    @ColumnInfo(name = "price")
    val price: Double = 0.0,
    @ColumnInfo(name = "price_add_coloring")
    val priceAddColoring: Double = 0.0,
    @ColumnInfo(name = "price_add_treatment")
    val priceAddTreatment: Double = 0.0,

    @ColumnInfo(name = "is_editable")
    val isEditable: Boolean = false,
    @ColumnInfo(name = "created")
    val created: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "updated")
    val updated: ZonedDateTime = ZonedDateTime.now(),

    // -------------------

    @ColumnInfo(name = "brand_id", index = true)
    val brandId: String = "",
    @ColumnInfo(name = "design_id", index = true)
    val designId: String = "",
    @ColumnInfo(name = "supplier_id", index = true)
    val supplierId: String = "",
    @ColumnInfo(name = "group_id", index = true)
    val groupId: String = "",
    @ColumnInfo(name = "specialty_id", index = true)
    val specialtyId: String = "",
    @ColumnInfo(name = "tech_id", index = true)
    val techId: String = "",
    @ColumnInfo(name = "type_id", index = true)
    val typeId: String = "",
    @ColumnInfo(name = "category_id", index = true)
    val categoryId: String = "",
    @ColumnInfo(name = "material_id", index = true)
    val materialId: String = "",

    @ColumnInfo(name = "default_treatment_id", index = true)
    val defaultTreatmentId: String = "",

    // add altHeights, colorings and treatments as n-m
) {
    companion object {
        const val tableName = "local_lenses"
    }
}
