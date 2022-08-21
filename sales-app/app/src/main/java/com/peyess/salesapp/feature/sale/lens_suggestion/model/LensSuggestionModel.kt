package com.peyess.salesapp.feature.sale.lens_suggestion.model

import androidx.compose.ui.text.capitalize

data class LensSuggestionModel(
    val id: String = "",

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

    val explanations: List<String> = listOf(),
)

fun LensSuggestionModel.name(): String {
    return "$brand $design $tech $material"
}
