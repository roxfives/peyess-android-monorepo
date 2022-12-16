package com.peyess.salesapp.data.adapter.disponibilities

import com.peyess.salesapp.data.model.lens.disponibility.DisponibilityDocument
import com.peyess.salesapp.data.model.lens.disponibility.FSDisponibility

fun FSDisponibility.toDisponibilityDocument(): DisponibilityDocument {
    return DisponibilityDocument(
        name = name,

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

        doc_version = doc_version,
        is_editable = is_editable,
        created = created,
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}