package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityWithoutManufacturersDocument
import com.peyess.salesapp.features.disponibility.contants.LensType
import com.peyess.salesapp.features.disponibility.model.Disponibility

fun StoreLensDisponibilityWithoutManufacturersDocument.toDisponibility(
    height: Double,
    lensType: LensType,
    alternativeHeights: List<StoreLensAltHeightDocument>,
): Disponibility {
    return Disponibility(
        diameter = diam,
        height = height,
        isLensTypeMono = lensType.isLensTypeMono(),
        maxCylindrical = maxCyl,
        minCylindrical = minCyl,
        maxSpherical = maxSph,
        minSpherical = minSph,
        maxAddition = maxAdd,
        minAddition = minAdd,
        hasPrism = hasPrism,
        prism = prism,
        sumRule = sumRule,
        hasAlternativeHeight = alternativeHeights.isNotEmpty(),
        alternativeHeights = alternativeHeights.map { it.toAlternativeHeight() },
    )
}
