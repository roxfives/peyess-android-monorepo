package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.LensPickModel
import java.math.BigDecimal

fun StoreLensWithDetailsDocument.toLensPickModel(): LensPickModel {
    return LensPickModel(
        id = id,
        family = brandName,
        description = designName,
        tech = techName,
        material = materialName,
        specialty = specialtyName,
        category = categoryName,
        supplier = supplierName,
        price = BigDecimal(price),
        observation = observation,
        warning = warning,
        hasFilterUv = hasFilterUv,
        hasFilterBlue = hasFilterBlue,

        explanations = explanations,

//        explanations = explanations,
//        isAvailable = isAvailable,
//        reasonsUnavailable = reasonsUnavailable,

//        missing
//        needsCheck = needsCheck,
    )
}