package com.peyess.salesapp.data.model.lens.room.dao.database_view

import androidx.room.DatabaseView
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
import java.time.ZonedDateTime

private const val lensesTable = LocalLensWithDetailsDBView.viewName
private const val disponibilitiesTable = LocalLensDisponibilityEntity.tableName

@DatabaseView(
    viewName = LocalLensDetailsWithDisponibilitiesDBView.viewName,
    value = """
        SELECT 
            lenses.*,
            
            disp.diam AS diameter,
            disp.max_cyl AS maxCylindrical,
            disp.min_cyl AS minCylindrical,
            disp.max_sph AS maxSpherical,
            disp.min_sph AS minSpherical,
            disp.max_add AS maxAddition,
            disp.min_add AS minAddition,
            disp.has_prism AS hasPrism,
            disp.PRISM AS prism,
            disp.prism_price AS prismPrice,
            disp.prism_cost AS prismCost,
            disp.separate_prism AS separatePrism,
            disp.needs_check AS dispNeedsCheck,
            disp.sum_rule AS sumRule
        FROM $lensesTable AS lenses
        JOIN $disponibilitiesTable AS disp ON lenses.id = disp.lens_id
    """,
)
data class LocalLensDetailsWithDisponibilitiesDBView(
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
) {
    companion object {
        const val viewName = "lenses_details_with_disponibilities"
    }
}