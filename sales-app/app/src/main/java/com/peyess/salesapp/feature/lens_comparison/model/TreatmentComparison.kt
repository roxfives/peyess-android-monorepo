package com.peyess.salesapp.feature.lens_comparison.model

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ceil

data class TreatmentComparison(
    val originalTreatment: Treatment = Treatment(),
    val pickedTreatment: Treatment = Treatment(),
) {
    fun priceDifference(
        withOriginal: Boolean,
        withPicked: Boolean,
        additionalOriginal: BigDecimal,
        additionalPicked: BigDecimal,
    ): BigDecimal {
        val picked = if (withPicked) { pickedTreatment.price } else { BigDecimal.ZERO }
        val original = if (withOriginal) { originalTreatment.price } else { BigDecimal.ZERO }

        // TODO: remove hardcoded string
        val addOriginal = if(
            originalTreatment.name.trim() == "Indisponível"
            || originalTreatment.name.trim() == "Incolor"
        ) {
            BigDecimal.ZERO
        } else {
            additionalOriginal
        }
        val addPicked = if(
            pickedTreatment.name.trim() == "Indisponível"
            || pickedTreatment.name.trim() == "Incolor"
        ) {
            BigDecimal.ZERO
        } else {
            additionalPicked
        }

        return (picked + addPicked - original - addOriginal)
            .setScale(0, RoundingMode.HALF_EVEN)
    }

    fun finalPrice(addPrice: Boolean, additional: BigDecimal): BigDecimal {
        // TODO: remove hardcoded string
        val add = if(
            pickedTreatment.name.trim() == "Indisponível"
            || pickedTreatment.name.trim() == "Incolor"
        ) {
            BigDecimal.ZERO
        } else {
            additional
        }

        return if (addPrice) {
            pickedTreatment.price + add
        } else {
            BigDecimal.ZERO
        }
    }
}
