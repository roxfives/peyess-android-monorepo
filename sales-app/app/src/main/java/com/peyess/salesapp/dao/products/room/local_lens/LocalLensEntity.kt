package com.peyess.salesapp.dao.products.room.local_lens

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalLensEntity.tableName)
data class LocalLensEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "is_local_enabled")
    val isLocalEnabled: Boolean = false,

    @ColumnInfo(name = "reason_local_disabled")
    val reasonLocalDisabled: String = "",

    @ColumnInfo(name = "price")
    val price: Double = 0.0,

    @ColumnInfo(name = "price_add_coloring")
    val priceAddColoring: Double = 0.0,

    @ColumnInfo(name = "price_add_treatment")
    val priceAddTreatment: Double = 0.0,

    @ColumnInfo(name = "height")
    val height: Double = 0.0,

    @ColumnInfo(name = "group")
    val group: String = "",
    @ColumnInfo(name = "group_id")
    val groupId: String = "",

    @ColumnInfo(name = "specialty")
    val specialty: String = "",
    @ColumnInfo(name = "specialty_id")
    val specialtyId: String = "",

    @ColumnInfo(name = "tech")
    val tech: String = "",
    @ColumnInfo(name = "tech_id")
    val techId: String = "",

    @ColumnInfo(name = "type")
    val type: String = "",
    @ColumnInfo(name = "type_id")
    val typeId: String = "",

    @ColumnInfo(name = "category")
    val category: String = "",
    @ColumnInfo(name = "category_id")
    val categoryId: String = "",

    @ColumnInfo(name = "material")
    val material: String = "",
    @ColumnInfo(name = "material_n")
    val materialN: Double = 0.0,
    @ColumnInfo(name = "material_id")
    val materialId: String = "",

    @ColumnInfo(name = "material_category")
    val materialCategory: String = "",
    @ColumnInfo(name = "material_category_id")
    val materialCategoryId: String = "",

    @ColumnInfo(name = "material_observation")
    val materialObservation: String = "",

    @ColumnInfo(name = "has_addition")
    val hasAddition: Boolean = false,

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

    @ColumnInfo(name = "default_treatment")
    val defaultTreatment: String = "",

    @ColumnInfo(name = "cost_add_coloring")
    val costAddColoring: Double = 0.0,

    @ColumnInfo(name = "cost_add_treatment")
    val costAddTreatment: Double = 0.0,

    @ColumnInfo(name = "suggested_price_add_coloring")
    val suggestedPriceAddColoring: Double = 0.0,

    @ColumnInfo(name = "suggested_price_add_treatment")
    val suggestedPriceAddTreatment: Double = 0.0,

    @ColumnInfo(name = "priority")
    val priority: Double = 0.0,

    @ColumnInfo(name = "is_generic")
    val isGeneric: Boolean = false,

    @ColumnInfo(name = "cost")
    val cost: Double = 0.0,
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

//    @ColumnInfo(name = "diam")
//    val diam: Double = 0.0,
//    @ColumnInfo(name = "max_cyl")
//    val maxCyl: Double = 0.0,
//    @ColumnInfo(name = "min_cyl")
//    val minCyl: Double = 0.0,
//    @ColumnInfo(name = "max_sph")
//    val maxSph: Double = 0.0,
//    @ColumnInfo(name = "min_sph")
//    val minSph: Double = 0.0,
//    @ColumnInfo(name = "max_add")
//    val maxAdd: Double = 0.0,
//    @ColumnInfo(name = "min_add")
//    val minAdd: Double = 0.0,
//
//    @ColumnInfo(name = "has_prism")
//    val hasPrism: Boolean = false,
//    @ColumnInfo(name = "prism")
//    val prism: Double = 0.0,
//    @ColumnInfo(name = "prism_price")
//    val prismPrice: Double = 0.0,
//    @ColumnInfo(name = "prism_cost")
//    val prismCost: Double = 0.0,
//    @ColumnInfo(name = "separate_prism")
//    val separatePrism: Boolean = false,
//
//    @ColumnInfo(name = "needs_check")
//    val needsCheck: Boolean = false,
//    @ColumnInfo(name = "sum_rule")
//    val sumRule: Boolean = false,
//    @ColumnInfo(name = "release_date")
//    val releaseDate: Date = Date(),
) {
    companion object {
        const val tableName = "local_lens"
    }
}


//   @ColumnInfo(name = "bases")
//   val bases: Double = 0.0,

//    manufacturers: Record<string, DispManufacturer>;
//    disp_manufacturers: string[];

//    material_types: Record<string, DenormilizedMaterialType>;
//    material_types_ids: string[];

//    val typeCategoriesIds: string[];
//    val typeCategories: Record<string, string>;

//    alt_heights_ids: string[];
//    alt_heights: Record<string, DenormilizedAlternativeHeight>;

//    @ColumnInfo(name = "explanations")
//    explanations: string[];

//    treatments_ids: string[];
//    treatments: Record<string, DenormilizedTreatment>;

//    colorings_ids: string[];
//    colorings: Record<string, DenormilizedColoring>;
