package com.peyess.salesapp.data.model.lens

import com.peyess.salesapp.data.model.lens.alt_height.FSLensAltHeight
import com.peyess.salesapp.data.model.lens.coloring.FSLensColoring
import com.peyess.salesapp.data.model.lens.material.FSLensMaterial
import com.peyess.salesapp.data.model.lens.treatment.FSLensTreatment
import com.peyess.salesapp.data.model.lens.disponibility.DisponibilityDocument
import java.time.ZonedDateTime
import java.util.Date

data class StoreLensDocument(
    val id: String = "",

    val bases: List<Double> = emptyList(),

    val disponibilities: List<DisponibilityDocument> = emptyList(),

    val dispManufacturers: List<String> = emptyList(),

    val height: Double = 0.0,

    val group: String = "",
    val groupId: String = "",

    val specialty: String = "",
    val specialtyId: String = "",

    val tech: String = "",
    val techId: String = "",

    val type: String = "",
    val typeId: String = "",
    val typeCategoriesIds: List<String> = emptyList(),
    val typeCategories: Map<String, String> = emptyMap(),

    val category: String = "",
    val categoryId: String = "",

    val material: String = "",
    val materialN: Double = 0.0,
    val materialId: String = "",
    val materialObservation: String = "",
    val materialCategory: String = "",
    val materialCategoryId: String = "",
    val materialTypes: Map<String, FSLensMaterial> = emptyMap(),
    val materialTypesIds: List<String> = emptyList(),

    val hasAddition: Boolean = false,

    val isColoringTreatmentMutex: Boolean = false,
    val isColoringDiscounted: Boolean = false,
    val isColoringIncluded: Boolean = false,
    val isTreatmentDiscounted: Boolean = false,
    val isTreatmentIncluded: Boolean = false,

    val altHeightsIds: List<String> = emptyList(),
    val altHeights: Map<String, FSLensAltHeight> = emptyMap(),

    val defaultTreatment: String = "",

    val treatmentsIds: List<String> = emptyList(),
    val treatments: Map<String, FSLensTreatment> = emptyMap(),

    val coloringsIds: List<String> = emptyList(),
    val colorings: Map<String, FSLensColoring> = emptyMap(),

    val costAddColoring: Double = 0.0,
    val costAddTreatment: Double = 0.0,

    val suggestedPriceAddColoring: Double = 0.0,
    val suggestedPriceAddTreatment: Double = 0.0,

    val priority: Double = 0.0,

    val isGeneric: Boolean = false,

    val cost: Double = 0.0,
    val suggestedPrice: Double = 0.0,

    val shippingTime: Double = 0.0,

    val explanations: List<String> = emptyList(),
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

    val isLocalEnabled: Boolean = true,
    val reasonLocalDisabled: String = "",
    val price: Double = 0.0,
    val priceAddColoring: Double = 0.0,
    val priceAddTreatment: Double = 0.0,

    val releaseDate: Date = Date(),

    val doc_version: Int = 0,
    val is_editable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val createdAt: Date = Date(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
