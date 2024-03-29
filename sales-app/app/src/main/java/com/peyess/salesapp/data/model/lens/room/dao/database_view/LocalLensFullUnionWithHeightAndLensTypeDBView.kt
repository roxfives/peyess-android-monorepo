package com.peyess.salesapp.data.model.lens.room.dao.database_view

import androidx.room.DatabaseView
import java.time.ZonedDateTime

private const val lensesFullUnionDBView = LocalLensFullUnionDBView.viewName

@DatabaseView(
    //TODO: Remove isLensMono dependency from name
    viewName = LocalLensFullUnionWithHeightAndLensTypeDBView.viewName,
    value = """
        SELECT *, 
            IIF(altHeightValue IS NULL, height, altHeightValue) AS heightResult,
            IIF(altHeightName IS NULL, "", altHeightName) AS altHeightName,
            IIF(altHeightNameDisplay IS NULL, "", altHeightNameDisplay) AS altHeightDisplay,
            IIF(altHeightObservation IS NULL, "", altHeightObservation) AS altHeightObservation,
            IIF(typeName LIKE '%multi%' OR typeName LIKE '%regressiv%', 0, 1) AS isLensMono
        FROM $lensesFullUnionDBView
    """,
)
data class LocalLensFullUnionWithHeightAndLensTypeDBView(
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

    val brandPriority: String = "",
    val designPriority: String = "",
    val supplierPriority: String = "",
    val groupPriority: String = "",
    val specialtyPriority: String = "",
    val techPriority: String = "",
    val typePriority: String = "",
    val categoryPriority: String = "",
    val materialPriority: String = "",

    val diameter: Double = 0.0,
    val maxCylindrical: Double = 0.0,
    val minCylindrical: Double = 0.0,
    val maxSpherical: Double = 0.0,
    val minSpherical: Double = 0.0,
    val maxAddition: Double = 0.0,
    val minAddition: Double = 0.0,
    val hasPrism: Double = 0.0,
    val prism: Double = 0.0,
    val prismPrice: Double = 0.0,
    val prismCost: Double = 0.0,
    val separatePrism: Double = 0.0,
    val dispNeedsCheck: Double = 0.0,
    val sumRule: Double = 0.0,

    val altHeightName: String = "",
    val altHeightNameDisplay: String = "",
    val altHeightValue: Double = 0.0,
    val altHeightObservation: String = "",

    val heightResult: Double = 0.0,
    val isLensMono: Boolean = false,
) {
    companion object {
        const val viewName = "lenses_full_union_with_height"
    }
}
