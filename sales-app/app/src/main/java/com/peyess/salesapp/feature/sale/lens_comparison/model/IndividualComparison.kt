package com.peyess.salesapp.feature.sale.lens_comparison.model

import androidx.annotation.RawRes
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.feature.sale.lens_comparison.utils.animationFromCategory

data class IndividualComparison(
    val id: Int = 0,
    val soId: String = "",

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

    @RawRes val animationId = animationFromCategory(lensComparison.pickedLens.materialCategory)
}

fun IndividualComparison.toLensComparison(): LensComparisonEntity {
    return LensComparisonEntity(
        id = id,
        soId = soId,

        originalLensId = lensComparison.originalLens.id,
        originalColoringId = coloringComparison.originalColoring.id,
        originalTreatmentId = treatmentComparison.originalTreatment.id,

        comparisonLensId = lensComparison.pickedLens.id,
        comparisonColoringId = coloringComparison.pickedColoring.id,
        comparisonTreatmentId = treatmentComparison.pickedTreatment.id,
    )
}
