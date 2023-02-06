package com.peyess.salesapp.screen.sale.lens_comparison.model

import kotlin.math.ceil

data class LensComparison(
    val originalLens: Lens = Lens(),
    val pickedLens: Lens = Lens(),
) {
    val originalAdditionalColoring = originalLens.priceAddColoring
    val originalAdditionalTreatment = originalLens.priceAddTreatment

    val pickedAdditionalColoring = pickedLens.priceAddColoring
    val pickedAdditionalTreatment = pickedLens.priceAddTreatment

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
