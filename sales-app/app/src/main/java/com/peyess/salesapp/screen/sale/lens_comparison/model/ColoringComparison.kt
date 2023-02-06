package com.peyess.salesapp.screen.sale.lens_comparison.model

import kotlin.math.ceil

data class ColoringComparison(
    val originalColoring: Coloring = Coloring(),
    val pickedColoring: Coloring = Coloring(),
) {
    val priceDifference = ceil(pickedColoring.price - originalColoring.price)
    val finalPrice = pickedColoring.price

    fun priceDifference(
        withOriginal: Boolean,
        withPicked: Boolean,
        additionalOriginal: Double,
        additionalPicked: Double,
    ): Double {
        val picked = if (withPicked) { pickedColoring.price } else { 0.0 }
        val original = if (withOriginal) { originalColoring.price } else { 0.0 }

        // TODO: remove hardcoded string
        val addOriginal = if(
            originalColoring.name.trim() == "Indisponível"
            || originalColoring.name.trim() == "Incolor"
        ) {
            0.0
        } else {
            additionalOriginal
        }
        val addPicked = if(
            pickedColoring.name.trim() == "Indisponível"
            || pickedColoring.name.trim() == "Incolor"
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
            pickedColoring.name.trim() == "Indisponível"
            || pickedColoring.name.trim() == "Incolor"
        ) {
            0.0
        } else {
            additional
        }

        return if (addPrice) {
            pickedColoring.price + add
        } else {
            0.0
        }
    }
}
