package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import kotlin.math.ceil

data class LensComparison(
    val originalLens: LocalLensEntity = LocalLensEntity(),
    val pickedLens: LocalLensEntity = LocalLensEntity(),
) {
    val priceDifference = ceil(pickedLens.price - originalLens.price)
    val finalPrice = pickedLens.price

    val addOriginalColoringPrice: Boolean = !originalLens.isColoringIncluded
            && !originalLens.isColoringDiscounted
    val addOriginalTreatmentPrice: Boolean = !originalLens.isTreatmentIncluded
            && !originalLens.isTreatmentDiscounted

    val addPickedColoringPrice: Boolean = !pickedLens.isColoringIncluded
            && !pickedLens.isColoringDiscounted
    val addPickedTreatmentPrice: Boolean = !pickedLens.isTreatmentIncluded
            && !pickedLens.isTreatmentDiscounted
}
