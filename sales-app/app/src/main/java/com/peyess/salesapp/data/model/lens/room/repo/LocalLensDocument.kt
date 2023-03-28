package com.peyess.salesapp.data.model.lens.room.repo

import java.time.ZonedDateTime

data class LocalLensDocument(
    val id: String = "",

    val priority: Double = 0.0,
    val storeLensPriority: Int = 0,

    val height: Double = 0.0,

    val hasAddition: Boolean = false,
    val hasFilterBlue: Boolean = false,
    val hasFilterUv: Boolean = false,
    val isColoringTreatmentMutex: Boolean = false,
    val isColoringDiscounted: Boolean = false,
    val isColoringIncluded: Boolean = false,
    val isTreatmentDiscounted: Boolean = false,
    val isTreatmentIncluded: Boolean = false,
    val isGeneric: Boolean = false,

    val shippingTime: Double = 0.0,

    val observation: String = "",
    val warning: String = "",

    val isManufacturingLocal: Boolean = false,
    val isEnabled: Boolean = false,
    val reasonDisabled: String = "",
    val isLocalEnabled: Boolean = true,
    val reasonLocalDisabled: String = "",

    val price: Double = 0.0,
    val priceAddColoring: Double = 0.0,
    val priceAddTreatment: Double = 0.0,

    val isEditable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val updated: ZonedDateTime = ZonedDateTime.now(),

    // -------------------

    val brandId: String = "",
    val designId: String = "",
    val supplierId: String = "",
    val groupId: String = "",
    val specialtyId: String = "",
    val techId: String = "",
    val typeId: String = "",
    val categoryId: String = "",
    val materialId: String = "",

    // ----

//    val explanations: List<String> = emptyList(),

    // -----------------------
//    val materialTypes: List<StoreLensMaterialTypeDocument> = emptyList(),
//    val materialTypesIds: List<String> = emptyList(),
//
//    val altHeightsIds: List<String> = emptyList(),
//    val altHeights: List<StoreLensAltHeightDocument> = emptyList(),
//
//    val coloringsIds: List<String> = emptyList(),
//    val colorings: List<StoreLensColoringDocument> = emptyList(),
//
//    val defaultTreatment: String = "",
//    val treatmentsIds: List<String> = emptyList(),
//    val treatments: List<StoreLensTreatmentDocument> = emptyList(),
//
//    val typeCategoriesIds: List<String> = emptyList(),
//    val typeCategories: List<LensTypeCategoryDocument> = emptyList(),
//
//    val bases: List<Double> = emptyList(),
//
//    val disponibilities: List<DisponibilityDocument> = emptyList(),
//    val dispManufacturers: List<String> = emptyList(),
)

