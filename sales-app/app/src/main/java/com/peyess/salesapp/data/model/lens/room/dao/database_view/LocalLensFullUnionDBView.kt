package com.peyess.salesapp.data.model.lens.room.dao.database_view

import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensAltHeightCrossRef
import java.time.ZonedDateTime

private const val lensesWithDispDBView = LocalLensDetailsWithDisponibilitiesDBView.viewName
private const val lensesWithAltHeightDBView = LocalLensWithDisponibilitiesAndAltHeightDBView.viewName

@DatabaseView(
    viewName = LocalLensFullUnionDBView.viewName,
    value = """
        SELECT lensesWithAltHeight.*
            FROM $lensesWithDispDBView AS lensesWithDisp
            LEFT OUTER JOIN $lensesWithAltHeightDBView AS lensesWithAltHeight 
                ON lensesWithDisp.id = lensesWithAltHeight.id
        UNION
        SELECT lensesWithAltHeight.*
            FROM $lensesWithAltHeightDBView AS lensesWithAltHeight
            LEFT OUTER JOIN $lensesWithDispDBView AS lensesWithDisp 
                ON lensesWithDisp.id = lensesWithAltHeight.id
    """,
)
data class LocalLensFullUnionDBView(
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
) {
    companion object {
        const val viewName = "lenses_full_union"
    }
}
