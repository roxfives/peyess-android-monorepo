package com.peyess.salesapp.feature.sale.lens_comparison.model

import kotlin.math.ceil

data class TreatmentComparison(
    val originalTreatment: Treatment = Treatment(),
    val pickedTreatment: Treatment = Treatment(),
) {
    fun priceDifference(
        withOriginal: Boolean,
        withPicked: Boolean,
        additionalOriginal: Double,
        additionalPicked: Double,
    ): Double {
        val picked = if (withPicked) { pickedTreatment.price } else { 0.0 }
        val original = if (withOriginal) { originalTreatment.price } else { 0.0 }

        // TODO: remove hardcoded string
        val addOriginal = if(
            originalTreatment.name.trim() == "Indisponível"
            || originalTreatment.name.trim() == "Incolor"
        ) {
            0.0
        } else {
            additionalOriginal
        }
        val addPicked = if(
            pickedTreatment.name.trim() == "Indisponível"
            || pickedTreatment.name.trim() == "Incolor"
        ) {
            0.0
        } else {
            additionalPicked
        }

        return ceil(picked + addPicked - original - addOriginal)
    }

    fun finalPrice(addPrice: Boolean, additional: Double): Double {
        // TODO: remove hardcoded string
        val add = if(
            pickedTreatment.name.trim() == "Indisponível"
            || pickedTreatment.name.trim() == "Incolor"
        ) {
            0.0
        } else {
            additional
        }

        return if (addPrice) {
            pickedTreatment.price + add
        } else {
            0.0
        }
    }
}
