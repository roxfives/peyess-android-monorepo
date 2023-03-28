package com.peyess.salesapp.data.adapter.lenses.disponibilities

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityWithoutManufacturersDocument

fun LocalLensDisponibilityEntity
        .toStoreLensDisponibilityDocument(): StoreLensDisponibilityWithoutManufacturersDocument {
    return StoreLensDisponibilityWithoutManufacturersDocument(
        diam = diam,
        maxCyl = maxCyl,
        minCyl = minCyl,
        maxSph = maxSph,
        minSph = minSph,
        maxAdd = maxAdd,
        minAdd = minAdd,
        hasPrism = hasPrism,
        prism = prism,
        prismPrice = prismPrice,
        prismCost = prismCost,
        separatePrism = separatePrism,
        needsCheck = needsCheck,
        sumRule = sumRule,
    )
}