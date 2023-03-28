package com.peyess.salesapp.data.adapter.disponibilities

import com.peyess.salesapp.data.model.lens.disponibility.FSDisponibility
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityDocument

fun FSDisponibility.toDisponibilityDocument(): StoreLensDisponibilityDocument {
    return StoreLensDisponibilityDocument(
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

        manufacturers = manufacturers.map {
            it.value.toDispManufacturerDocument(it.key)
        },
    )
}