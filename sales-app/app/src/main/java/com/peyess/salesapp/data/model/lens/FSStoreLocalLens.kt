package com.peyess.salesapp.data.model.lens

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.lens.disponibility.FSDisponibility
import com.peyess.salesapp.data.model.lens.alt_height.FSStoreLensAltHeight
import com.peyess.salesapp.data.model.lens.coloring.FSStoreLensColoring
import com.peyess.salesapp.data.model.lens.treatment.FSLensTreatment
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
import com.peyess.salesapp.data.model.lens.material_type.FSMaterialType
import java.util.Date

@Keep
@IgnoreExtraProperties
data class FSStoreLocalLens(
    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("brand_priority")
    val brandPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("design_priority")
    val designPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("material_priority")
    val materialPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("tech_priority")
    val techPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("specialty_priority")
    val specialtyPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("type_priority")
    val typePriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("group_priority")
    val groupPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("supplier_priority")
    val supplierPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("category_priority")
    val categoryPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("material_category_priority")
    val materialCategoryPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_lens_priority")
    val storeLensPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_brand_priority")
    val storeBrandPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_design_priority")
    val storeDesignPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_material_priority")
    val storeMaterialPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_tech_priority")
    val storeTechPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_specialty_priority")
    val storeSpecialtyPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_type_priority")
    val storeTypePriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_group_priority")
    val storeGroupPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_supplier_priority")
    val storeSupplierPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_category_priority")
    val storeCategoryPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("store_material_category_priority")
    val storeMaterialCategoryPriority: Int = 0,

    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("store_id")
    val storeId: String = "",

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
    val materialTypes: Map<String, FSMaterialType> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("material_types_ids")
    val materialTypesIds: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("needs_check")
    val needsCheck: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("has_addition")
    val hasAddition: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("has_filter_blue")
    val hasFilterBlue: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("has_filter_uv")
    val hasFilterUv: Boolean = false,

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
    val altHeights: Map<String, FSStoreLensAltHeight> = emptyMap(),

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
    val colorings: Map<String, FSStoreLensColoring> = emptyMap(),

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
    @PropertyName("doc_version")
    val docVersion: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val isEditable: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)

fun FSStoreLocalLens.getExplanations(): List<LocalProductExpEntity> {
    return explanations.map {
        LocalProductExpEntity(
            productId = id,
            exp = it,
        )
    }
}

fun FSStoreLocalLens.toFilterCategory(): FilterLensCategoryEntity {
    return FilterLensCategoryEntity(
        id = categoryId,
        lensId = id,
        name = category,
    )
}

fun FSStoreLocalLens.toFilterLensCategory(): FilterLensCategoryEntity {
    return FilterLensCategoryEntity(
        id = categoryId,
        lensId = id,
        name = category,
    )
}

fun FSStoreLocalLens.toFilterLensDescription(): FilterLensDescriptionEntity {
    return FilterLensDescriptionEntity(
        id = designId,
        name = design,
        supplierId = supplierId,
    )
}

fun FSStoreLocalLens.toFilterLensFamily(): FilterLensFamilyEntity {
    return FilterLensFamilyEntity(
        id = brandId,
        name = brand,
        supplierId = supplierId,
    )
}

fun FSStoreLocalLens.toFilterLensGroup(): FilterLensGroupEntity {
    return FilterLensGroupEntity(
        id = groupId,
        lensId = id,
        name = group,
    )
}

fun FSStoreLocalLens.toFilterLensMaterial(): FilterLensMaterialEntity {
    return FilterLensMaterialEntity(
        id = materialId,
        supplierId = supplierId,
        name = material,
    )
}

fun FSStoreLocalLens.toFilterLensSpecialty(): FilterLensSpecialtyEntity {
    return FilterLensSpecialtyEntity(
        id = specialtyId,
        name = specialty,
    )
}

fun FSStoreLocalLens.toFilterLensSupplier(): FilterLensSupplierEntity {
    return FilterLensSupplierEntity(
        id = supplierId,
        name = supplier,
    )
}

fun FSStoreLocalLens.toFilterLensTech(): FilterLensTechEntity {
    return FilterLensTechEntity(
        id = techId,
        name = tech,
    )
}

fun FSStoreLocalLens.toFilterLensType(): FilterLensTypeEntity {
    return FilterLensTypeEntity(
        id = typeId,
        name = type,
    )
}

fun FSStoreLocalLens.toLocalLensEntity(): LocalLensEntity {
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
