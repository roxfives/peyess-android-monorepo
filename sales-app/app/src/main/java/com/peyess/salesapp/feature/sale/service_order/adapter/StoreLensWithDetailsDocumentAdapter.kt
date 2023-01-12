package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.feature.sale.service_order.model.Lens

fun StoreLensWithDetailsDocument.toLens(): Lens {
    return Lens(
        id = id,
        hasAddition = hasAddition,
        hasFilterBlue = hasFilterBlue,
        hasFilterUv = hasFilterUv,
        isColoringTreatmentMutex = isColoringTreatmentMutex,
        isColoringDiscounted = isColoringDiscounted,
        isColoringIncluded = isColoringIncluded,
        isTreatmentDiscounted = isTreatmentDiscounted,
        isTreatmentIncluded = isTreatmentIncluded,
        needsCheck = needsCheck,
        shippingTime = shippingTime,
        observation = observation,
        warning = warning,
        price = price,
        priceAddColoring = priceAddColoring,
        priceAddTreatment = priceAddTreatment,
        brandName = brandName,
        designName = designName,
        supplierName = supplierName,
        groupName = groupName,
        specialtyName = specialtyName,
        techName = techName,
        typeName = typeName,
        categoryName = categoryName,
        materialName = materialName,
        explanations = explanations,
    )
}
