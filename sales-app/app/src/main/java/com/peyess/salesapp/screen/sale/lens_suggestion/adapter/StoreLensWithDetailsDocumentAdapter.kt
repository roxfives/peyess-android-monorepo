package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensPickModel
import java.math.BigDecimal

fun StoreLensWithDetailsDocument.toLensPickModel(
    reasonsUnavailable: List<String> = emptyList(),
): LensPickModel {
    return LensPickModel(
        id = id,
        family = brandName,
        description = designName,
        tech = techName,
        material = materialName,
        specialty = specialtyName,
        group = groupName,
        supplier = supplierName,
        price = price,
        observation = observation,
        warning = warning,
        hasFilterUv = hasFilterUv,
        hasFilterBlue = hasFilterBlue,

        explanations = explanations,

        needsCheck = needsCheck,
        reasonsUnavailable = reasonsUnavailable,
    )
}