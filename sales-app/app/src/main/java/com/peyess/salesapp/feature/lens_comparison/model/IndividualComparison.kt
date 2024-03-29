package com.peyess.salesapp.feature.lens_comparison.model

import androidx.annotation.RawRes
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.feature.lens_comparison.utils.animationFromCategory
import com.peyess.salesapp.feature.lens_comparison.utils.bigAnimationFromCategory
import java.math.BigDecimal

data class IndividualComparison(
    val id: Int = 0,
    val soId: String = "",

    val prescription: Prescription = Prescription(),

    val lensComparison: LensComparison = LensComparison(),
    val coloringComparison: ColoringComparison = ColoringComparison(),
    val treatmentComparison: TreatmentComparison = TreatmentComparison(),
) {
    val finalPriceDifference: BigDecimal = lensComparison.priceDifference +
            coloringComparison.priceDifference(
                lensComparison.addOriginalColoringPrice,
                lensComparison.addPickedColoringPrice,
                lensComparison.originalAdditionalColoring,
                lensComparison.pickedAdditionalColoring,
            ) +
            treatmentComparison.priceDifference(
                lensComparison.addOriginalTreatmentPrice,
                lensComparison.addPickedTreatmentPrice,
                lensComparison.originalAdditionalTreatment,
                lensComparison.pickedAdditionalTreatment,
            )

    val finalPrice = lensComparison.finalPrice +
            coloringComparison.finalPrice(
                lensComparison.addPickedColoringPrice,
                lensComparison.pickedAdditionalColoring,
            ) +
            treatmentComparison.finalPrice(
                lensComparison.addPickedTreatmentPrice,
                lensComparison.pickedAdditionalTreatment,
            )

    val isPriceBad = finalPriceDifference < BigDecimal.ZERO

    @RawRes
    val animationId = animationFromCategory(lensComparison.pickedLens.materialCategory, prescription)
    @RawRes
    val bigAnimationId = bigAnimationFromCategory(lensComparison.pickedLens.materialCategory, prescription)
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
