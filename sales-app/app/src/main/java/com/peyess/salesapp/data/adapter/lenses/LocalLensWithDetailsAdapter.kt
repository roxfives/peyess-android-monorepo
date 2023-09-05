package com.peyess.salesapp.data.adapter.lenses

import com.peyess.salesapp.data.adapter.lenses.alt_height.toStoreLensAltHeightDocument
import com.peyess.salesapp.data.adapter.lenses.disponibilities.toStoreLensDisponibilityDocument
import com.peyess.salesapp.data.model.lens.room.dao.embedded.LocalLensWithDetails
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import java.math.BigDecimal

fun LocalLensWithDetails.toStoreLensWithDetailsDocument(): StoreLensWithDetailsDocument {
    return StoreLensWithDetailsDocument(
        id = lens.id,
        priority = lens.priority,
        storeLensPriority = lens.storeLensPriority,
        height = lens.height,
        hasAddition = lens.hasAddition,
        hasFilterBlue = lens.hasFilterBlue,
        hasFilterUv = lens.hasFilterUv,
        isColoringTreatmentMutex = lens.isColoringTreatmentMutex,
        isColoringDiscounted = lens.isColoringDiscounted,
        isColoringIncluded = lens.isColoringIncluded,
        isTreatmentDiscounted = lens.isTreatmentDiscounted,
        isTreatmentIncluded = lens.isTreatmentIncluded,
        isGeneric = lens.isGeneric,
        needsCheck = lens.needsCheck,
        shippingTime = lens.shippingTime,
        observation = lens.observation,
        warning = lens.warning,
        isManufacturingLocal = lens.isManufacturingLocal,
        isEnabled = lens.isEnabled,
        reasonDisabled = lens.reasonDisabled,
        isLocalEnabled = lens.isLocalEnabled,
        reasonLocalDisabled = lens.reasonLocalDisabled,
        price = lens.price.toBigDecimal(),
        priceAddColoring = lens.priceAddColoring.toBigDecimal(),
        priceAddTreatment = lens.priceAddTreatment.toBigDecimal(),
        isEditable = lens.isEditable,
        created = lens.created,
        updated = lens.updated,
        brandId = lens.brandId,
        designId = lens.designId,
        supplierId = lens.supplierId,
        groupId = lens.groupId,
        specialtyId = lens.specialtyId,
        techId = lens.techId,
        typeId = lens.typeId,
        categoryId = lens.categoryId,
        materialId = lens.materialId,
        defaultTreatmentId = lens.defaultTreatmentId,
        brandName = lens.brandName,
        designName = lens.designName,
        supplierName = lens.supplierName,
        groupName = lens.groupName,
        specialtyName = lens.specialtyName,
        techName = lens.techName,
        typeName = lens.typeName,
        categoryName = lens.categoryName,
        materialName = lens.materialName,
        brandPriority = lens.brandPriority,
        designPriority = lens.designPriority,
        supplierPriority = lens.supplierPriority,
        groupPriority = lens.groupPriority,
        specialtyPriority = lens.specialtyPriority,
        techPriority = lens.techPriority,
        typePriority = lens.typePriority,
        categoryPriority = lens.categoryPriority,
        materialPriority = lens.materialPriority,
        materialCategory = lens.materialCategory,

        disponibilities = disponibilities.map { it.toStoreLensDisponibilityDocument() },
        altHeights = altHeights.map { it.toStoreLensAltHeightDocument() },

        explanations = explanations.map { it.explanation },
    )
}