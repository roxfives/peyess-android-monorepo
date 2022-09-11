package com.peyess.salesapp.feature.sale.lens_pick.model

import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispEntity

data class LensSuggestionModel(
    val id: String = "",

    val supportedDisponibilitites: List<LocalLensDispEntity> = listOf(),
    val needsCheck: Boolean = false,

    val isLocalEnabled: Boolean = false,

    val reasonLocalDisabled: String = "",

    val price: Double = 0.0,

    val priceAddColoring: Double = 0.0,

    val priceAddTreatment: Double = 0.0,

    val height: Double = 0.0,

    val group: String = "",
    val groupId: String = "",

    val specialty: String = "",
    val specialtyId: String = "",

    val tech: String = "",
    val techId: String = "",

    val type: String = "",
    val typeId: String = "",

    val category: String = "",
    val categoryId: String = "",

    val material: String = "",
    val materialN: Double = 0.0,
    val materialId: String = "",

    val materialCategory: String = "",
    val materialCategoryId: String = "",

    val materialObservation: String = "",

    val hasAddition: Boolean = false,

    val isColoringTreatmentMutex: Boolean = false,

    val isColoringDiscounted: Boolean = false,

    val isColoringIncluded: Boolean = false,

    val isTreatmentDiscounted: Boolean = false,

    val isTreatmentIncluded: Boolean = false,

    val defaultTreatment: String = "",

    val costAddColoring: Double = 0.0,

    val costAddTreatment: Double = 0.0,

    val suggestedPriceAddColoring: Double = 0.0,

    val suggestedPriceAddTreatment: Double = 0.0,

    val priority: Double = 0.0,

    val isGeneric: Boolean = false,

    val cost: Double = 0.0,
    val suggestedPrice: Double = 0.0,

    val shippingTime: Double = 0.0,

    val observation: String = "",
    val warning: String = "",

    val brand: String = "",
    val brandId: String = "",
    val design: String = "",
    val designId: String = "",

    val supplierPicture: String = "",
    val supplier: String = "",
    val supplierId: String = "",

    val isManufacturingLocal: Boolean = false,
    val isEnabled: Boolean = false,
    val reasonDisabled: String = "",

    val explanations: List<String> = emptyList(),

    val reasonsNotAvailable: List<String> = emptyList(),
)

fun LensSuggestionModel.name(): String {
    return "$brand $design $tech $material"
}

fun LocalLensEntity.toSuggestionModel(
    exp: List<String>,
    reasonsNotAvailable: List<String>,
): LensSuggestionModel {
    return LensSuggestionModel(
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

        explanations = exp,
        reasonsNotAvailable = reasonsNotAvailable,
    )
}
