package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityDocument

fun StoreLensDisponibilityDocument.toLocalLensDisponibilityEntity(
    lensId: String,
): LocalLensDisponibilityEntity {
    return LocalLensDisponibilityEntity(
        id = 0L,
        lensId = lensId,
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
