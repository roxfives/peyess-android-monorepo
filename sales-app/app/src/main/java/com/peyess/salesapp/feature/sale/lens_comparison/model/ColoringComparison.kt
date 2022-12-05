package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_coloring.name
import kotlin.math.ceil

data class ColoringComparison(
    val originalColoring: LocalColoringEntity = LocalColoringEntity(),
    val pickedColoring: LocalColoringEntity = LocalColoringEntity(),
) {
    // TODO: update to price instead of suggestedPrice
    val priceDifference = ceil(pickedColoring.suggestedPrice - originalColoring.suggestedPrice)
    val finalPrice = pickedColoring.suggestedPrice

    fun priceDifference(
        withOriginal: Boolean,
        withPicked: Boolean,
        additionalOriginal: Double,
        additionalPicked: Double,
    ): Double {
        val picked = if (withPicked) { pickedColoring.suggestedPrice } else { 0.0 }
        val original = if (withOriginal) { originalColoring.suggestedPrice } else { 0.0 }

        // TODO: remove hardcoded string
        val addOriginal = if(
            originalColoring.name().trim() == "Indisponível"
            || originalColoring.name().trim() == "Incolor"
        ) {
            0.0
        } else {
            additionalOriginal
        }
        val addPicked = if(
            pickedColoring.name().trim() == "Indisponível"
            || pickedColoring.name().trim() == "Incolor"
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
            pickedColoring.name().trim() == "Indisponível"
            || pickedColoring.name().trim() == "Incolor"
        ) {
            0.0
        } else {
            additional
        }

        return if (addPrice) {
            pickedColoring.suggestedPrice + add
        } else {
            0.0
        }
    }
}
