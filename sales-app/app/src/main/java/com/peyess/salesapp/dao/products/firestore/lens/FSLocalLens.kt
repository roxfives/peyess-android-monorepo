package com.peyess.salesapp.dao.products.firestore.lens

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.dao.products.firestore.disponibility.FSDisponibility
import com.peyess.salesapp.dao.products.firestore.lens_alt_height.FSLensAltHeight
import com.peyess.salesapp.dao.products.firestore.lens_coloring.FSLensColoring
import com.peyess.salesapp.dao.products.firestore.lens_material.FSLensMaterialType
import com.peyess.salesapp.dao.products.firestore.lens_treatment.FSLensTreatment
import com.peyess.salesapp.dao.products.room.filter_lens_category.FilterLensCategoryEntity
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionEntity
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_group.FilterLensGroupEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpEntity
import java.util.Date

data class FSLocalLens(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("bases")
    val bases: List<Double> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("disponibilities")
    val disponibilities: List<FSDisponibility> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("disp_manufacturers")
    val dispManufacturers: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("height")
    val height: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("group")
    val group: String = "",
    @Keep
    @JvmField
    @PropertyName("group_id")
    val groupId: String = "",

    @Keep
    @JvmField
    @PropertyName("specialty")
    val specialty: String = "",
    @Keep
    @JvmField
    @PropertyName("specialty_id")
    val specialtyId: String = "",

    @Keep
    @JvmField
    @PropertyName("tech")
    val tech: String = "",
    @Keep
    @JvmField
    @PropertyName("tech_id")
    val techId: String = "",

    @Keep
    @JvmField
    @PropertyName("type")
    val type: String = "",
    @Keep
    @JvmField
    @PropertyName("type_id")
    val typeId: String = "",
    @Keep
    @JvmField
    @PropertyName("type_categories_ids")
    val typeCategoriesIds: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("type_categories")
    val typeCategories: Map<String, String> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("category")
    val category: String = "",
    @Keep
    @JvmField
    @PropertyName("category_id")
    val categoryId: String = "",

    @Keep
    @JvmField
    @PropertyName("material")
    val material: String = "",
    @Keep
    @JvmField
    @PropertyName("material_n")
    val materialN: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("material_id")
    val materialId: String = "",
    @Keep
    @JvmField
    @PropertyName("material_observation")
    val materialObservation: String = "",
    @Keep
    @JvmField
    @PropertyName("material_category")
    val materialCategory: String = "",
    @Keep
    @JvmField
    @PropertyName("material_category_id")
    val materialCategoryId: String = "",
    @Keep
    @JvmField
    @PropertyName("material_types")
    val materialTypes: Map<String, FSLensMaterialType> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("material_types_ids")
    val materialTypesIds: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("has_addition")
    val hasAddition: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_coloring_treatment_mutex")
    val isColoringTreatmentMutex: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("is_coloring_discounted")
    val isColoringDiscounted: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("is_coloring_included")
    val isColoringIncluded: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("is_treatment_discounted")
    val isTreatmentDiscounted: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("is_treatment_included")
    val isTreatmentIncluded: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("alt_heights_ids")
    val altHeightsIds: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("alt_heights")
    val altHeights: Map<String, FSLensAltHeight> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("default_treatment")
    val defaultTreatment: String = "",

    @Keep
    @JvmField
    @PropertyName("treatments_ids")
    val treatmentsIds: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("treatments")
    val treatments: Map<String, FSLensTreatment> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("colorings_ids")
    val coloringsIds: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("colorings")
    val colorings: Map<String, FSLensColoring> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("cost_add_coloring")
    val costAddColoring: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("cost_add_treatment")
    val costAddTreatment: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("suggested_price_add_coloring")
    val suggestedPriceAddColoring: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("suggested_price_add_treatment")
    val suggestedPriceAddTreatment: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("is_generic")
    val isGeneric: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("cost")
    val cost: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("suggested_price")
    val suggestedPrice: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("shipping_time")
    val shippingTime: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("explanations")
    val explanations: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("observation")
    val observation: String = "",
    @Keep
    @JvmField
    @PropertyName("warning")
    val warning: String = "",

    @Keep
    @JvmField
    @PropertyName("brand")
    val brand: String = "",
    @Keep
    @JvmField
    @PropertyName("brand_id")
    val brandId: String = "",
    @Keep
    @JvmField
    @PropertyName("design")
    val design: String = "",
    @Keep
    @JvmField
    @PropertyName("design_id")
    val designId: String = "",

    @Keep
    @JvmField
    @PropertyName("supplier_picture")
    val supplierPicture: String = "",
    @Keep
    @JvmField
    @PropertyName("supplier")
    val supplier: String = "",
    @Keep
    @JvmField
    @PropertyName("supplier_id")
    val supplierId: String = "",

    @Keep
    @JvmField
    @PropertyName("is_manufacturing_local")
    val isManufacturingLocal: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("is_enabled")
    val isEnabled: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("reason_disabled")
    val reasonDisabled: String = "",

    @Keep
    @JvmField
    @PropertyName("is_local_enabled")
    val isLocalEnabled: Boolean = true,
    @Keep
    @JvmField
    @PropertyName("reason_local_disabled")
    val reasonLocalDisabled: String = "",
    @Keep
    @JvmField
    @PropertyName("price")
    val price: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("price_add_coloring")
    val priceAddColoring: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("price_add_treatment")
    val priceAddTreatment: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("release_date")
    val releaseDate: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("created_at")
    val createdAt: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("created_allowed_by")
    val createdAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated_allowed_by")
    val updatedAllowedBy: String = "",
)

fun FSLocalLens.getExplanations(): List<LocalProductExpEntity> {
    return explanations.map {
        LocalProductExpEntity(
            productId = id,
            exp = it,
        )
    }
}

fun FSLocalLens.toFilterCategory(): FilterLensCategoryEntity {
    return FilterLensCategoryEntity(
        id = categoryId,
        lensId = id,
        name = category,
    )
}

fun FSLocalLens.toFilterLensCategory(): FilterLensCategoryEntity {
    return FilterLensCategoryEntity(
        id = categoryId,
        lensId = id,
        name = category,
    )
}

fun FSLocalLens.toFilterLensDescription(): FilterLensDescriptionEntity {
    return FilterLensDescriptionEntity(
        id = designId,
        name = design,
    )
}

fun FSLocalLens.toFilterLensFamily(): FilterLensFamilyEntity {
    return FilterLensFamilyEntity(
        id = brandId,
        name = brand,
        supplierId = supplierId,
    )
}

fun FSLocalLens.toFilterLensGroup(): FilterLensGroupEntity {
    return FilterLensGroupEntity(
        id = groupId,
        lensId = id,
        name = group,
    )
}

fun FSLocalLens.toFilterLensMaterial(): FilterLensMaterialEntity {
    return FilterLensMaterialEntity(
        id = materialId,
        supplierId = supplierId,
        name = material,
    )
}

fun FSLocalLens.toFilterLensSpecialty(): FilterLensSpecialtyEntity {
    return FilterLensSpecialtyEntity(
        id = specialtyId,
        lensId = id,
        name = specialty,
    )
}

fun FSLocalLens.toFilterLensSupplier(): FilterLensSupplierEntity {
    return FilterLensSupplierEntity(
        id = supplierId,
        name = supplier,
    )
}

fun FSLocalLens.toFilterLensTech(): FilterLensTechEntity {
    return FilterLensTechEntity(
        id = techId,
        name = tech,
    )
}

fun FSLocalLens.toFilterLensType(): FilterLensTypeEntity {
    return FilterLensTypeEntity(
        id = typeId,
        name = type,
    )
}

fun FSLocalLens.toLocalLensEntity(): LocalLensEntity {
    return LocalLensEntity(
        id = id,
        isLocalEnabled = isLocalEnabled,
        reasonLocalDisabled = reasonLocalDisabled,
        price = price,
        priceAddColoring = priceAddColoring,
        priceAddTreatment = priceAddTreatment,
        height = height,
        group = group,
        groupId = groupId,
        specialty = specialty,
        specialtyId = specialtyId,
        tech = tech,
        techId = techId,
        type = type,
        typeId = typeId,
        category = category,
        categoryId = categoryId,
        material = material,
        materialN = materialN,
        materialId = materialId,
        materialCategory = materialCategory,
        materialCategoryId = materialCategoryId,
        materialObservation = materialObservation,
        hasAddition = hasAddition,
        isColoringTreatmentMutex = isColoringTreatmentMutex,
        isColoringDiscounted = isColoringDiscounted,
        isColoringIncluded = isColoringIncluded,
        isTreatmentDiscounted = isTreatmentDiscounted,
        isTreatmentIncluded = isTreatmentIncluded,
        defaultTreatment = defaultTreatment,
        costAddColoring = costAddColoring,
        costAddTreatment = costAddTreatment,
        suggestedPriceAddColoring = suggestedPriceAddColoring,
        suggestedPriceAddTreatment = suggestedPriceAddTreatment,
        priority = priority,
        isGeneric = isGeneric,
        cost = cost,
        suggestedPrice = suggestedPrice,
        shippingTime = shippingTime,
        observation = observation,
        warning = warning,
        brand = brand,
        brandId = brandId,
        design = design,
        designId = designId,
        supplierPicture = supplierPicture,
        supplier = supplier,
        supplierId = supplierId,
        isManufacturingLocal = isManufacturingLocal,
        isEnabled = isEnabled,
        reasonDisabled = reasonDisabled,
    )
}

