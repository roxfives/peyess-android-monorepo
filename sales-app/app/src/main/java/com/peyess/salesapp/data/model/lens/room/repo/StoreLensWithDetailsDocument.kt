package com.peyess.salesapp.data.model.lens.room.repo

import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import java.math.BigDecimal
import java.time.ZonedDateTime

data class StoreLensWithDetailsDocument(
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
    val needsCheck: Boolean = false,

    val shippingTime: Double = 0.0,

    val observation: String = "",
    val warning: String = "",

    val isManufacturingLocal: Boolean = false,
    val isEnabled: Boolean = false,
    val reasonDisabled: String = "",
    val isLocalEnabled: Boolean = true,
    val reasonLocalDisabled: String = "",

    val price: BigDecimal = BigDecimal.ZERO,
    val priceAddColoring: BigDecimal = BigDecimal.ZERO,
    val priceAddTreatment: BigDecimal = BigDecimal.ZERO,

    val isEditable: Boolean = false,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val updated: ZonedDateTime = ZonedDateTime.now(),

    val brandId: String = "",
    val designId: String = "",
    val supplierId: String = "",
    val groupId: String = "",
    val specialtyId: String = "",
    val techId: String = "",
    val typeId: String = "",
    val categoryId: String = "",
    val materialId: String = "",
    val defaultTreatmentId: String = "",

    val brandName: String = "",
    val designName: String = "",
    val supplierName: String = "",
    val groupName: String = "",
    val specialtyName: String = "",
    val techName: String = "",
    val typeName: String = "",
    val categoryName: String = "",
    val materialName: String = "",

    val materialCategory: String = "",

    val brandPriority: String = "",
    val designPriority: String = "",
    val supplierPriority: String = "",
    val groupPriority: String = "",
    val specialtyPriority: String = "",
    val techPriority: String = "",
    val typePriority: String = "",
    val categoryPriority: String = "",
    val materialPriority: String = "",

    val disponibilities: List<StoreLensDisponibilityWithoutManufacturersDocument> = emptyList(),
    val altHeights: List<StoreLensAltHeightDocument> = emptyList(),

    val explanations: List<String> = emptyList(),
) {
    val name = "$brandName $designName $techName $materialName"
}
