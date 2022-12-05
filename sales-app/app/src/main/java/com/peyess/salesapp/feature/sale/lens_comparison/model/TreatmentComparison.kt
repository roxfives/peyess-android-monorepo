package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_coloring.name
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_treatment.name
import kotlin.math.ceil

data class TreatmentComparison(
    val originalTreatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val pickedTreatment: LocalTreatmentEntity = LocalTreatmentEntity(),
) {
    // TODO: update to price instead of suggestedPrice
    fun priceDifference(
        withOriginal: Boolean,
        withPicked: Boolean,
        additionalOriginal: Double,
        additionalPicked: Double,
    ): Double {
        val picked = if (withPicked) { pickedTreatment.suggestedPrice } else { 0.0 }
        val original = if (withOriginal) { originalTreatment.suggestedPrice } else { 0.0 }

        // TODO: remove hardcoded string
        val addOriginal = if(
            originalTreatment.name().trim() == "Indisponível"
            || originalTreatment.name().trim() == "Incolor"
        ) {
            0.0
        } else {
            additionalOriginal
        }
        val addPicked = if(
            pickedTreatment.name().trim() == "Indisponível"
            || pickedTreatment.name().trim() == "Incolor"
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
            pickedTreatment.name().trim() == "Indisponível"
            || pickedTreatment.name().trim() == "Incolor"
        ) {
            0.0
        } else {
            additional
        }

        return if (addPrice) {
            pickedTreatment.suggestedPrice + add
        } else {
            0.0
        }
    }
}
