package com.peyess.salesapp.data.adapter.disponibilities

import com.peyess.salesapp.data.model.lens.disponibility.DisponibilityDocument
import com.peyess.salesapp.data.model.lens.disponibility.FSDisponibility

fun FSDisponibility.toDisponibilityDocument(): DisponibilityDocument {
    return DisponibilityDocument(
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

        manufacturers = manufacturers.mapValues {
            it.value.toDispManufacturerDocument()
        },
    )
}