package com.peyess.salesapp.feature.sale.lens_comparison.model

data class IndividualComparison(
    val lensComparison: LensComparison = LensComparison(),
    val coloringComparison: ColoringComparison = ColoringComparison(),
    val treatmentComparison: TreatmentComparison = TreatmentComparison(),
) {
    val finalPriceDifference = lensComparison.priceDifference +
            coloringComparison.priceDifference +
            treatmentComparison.priceDifference

    val finalPrice = lensComparison.finalPrice +
            coloringComparison.finalPrice +
            treatmentComparison.finalPrice

    val isPriceBad = finalPriceDifference < 0
}
