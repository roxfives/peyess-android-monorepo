package com.peyess.salesapp.feature.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.feature.sale.lens_comparison.model.Lens

fun StoreLensWithDetailsDocument.toLens(): Lens {
    return Lens(
        id = id,
        priority = priority,
        storeLensPriority = storeLensPriority,
        hasFilterBlue = hasFilterBlue,
        hasFilterUv = hasFilterUv,
        isColoringTreatmentMutex = isColoringTreatmentMutex,
        isColoringDiscounted = isColoringDiscounted,
        isColoringIncluded = isColoringIncluded,
        isTreatmentDiscounted = isTreatmentDiscounted,
        isTreatmentIncluded = isTreatmentIncluded,
        isGeneric = isGeneric,
        needsCheck = needsCheck,
        shippingTime = shippingTime,
        observation = observation,
        warning = warning,
        price = price,
        priceAddColoring = priceAddColoring,
        priceAddTreatment = priceAddTreatment,
        brandId = brandId,
        designId = designId,
        supplierId = supplierId,
        groupId = groupId,
        specialtyId = specialtyId,
        techId = techId,
        typeId = typeId,
        categoryId = categoryId,
        materialId = materialId,
        defaultTreatmentId = defaultTreatmentId,
        brandName = brandName,
        designName = designName,
        supplierName = supplierName,
        groupName = groupName,
        specialtyName = specialtyName,
        techName = techName,
        typeName = typeName,
        categoryName = categoryName,
        materialName = materialName,
        materialCategory = materialCategory,
        explanations = explanations,
    )
}