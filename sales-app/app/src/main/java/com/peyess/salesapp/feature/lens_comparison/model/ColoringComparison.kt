package com.peyess.salesapp.feature.lens_comparison.model

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ceil

data class ColoringComparison(
    val originalColoring: Coloring = Coloring(),
    val pickedColoring: Coloring = Coloring(),
) {
    val priceDifference : BigDecimal = (pickedColoring.price - originalColoring.price)
        .setScale(0, RoundingMode.CEILING)
    val finalPrice = pickedColoring.price

    fun priceDifference(
        withOriginal: Boolean,
        withPicked: Boolean,
        additionalOriginal: BigDecimal,
        additionalPicked: BigDecimal,
    ): BigDecimal {
        val picked = if (withPicked) { pickedColoring.price } else { BigDecimal.ZERO }
        val original = if (withOriginal) { originalColoring.price } else { BigDecimal.ZERO }

        // TODO: remove hardcoded string
        val addOriginal = if(
            originalColoring.name.trim() == "Indisponível"
            || originalColoring.name.trim() == "Incolor"
        ) {
            BigDecimal.ZERO
        } else {
            additionalOriginal
        }
        val addPicked = if(
            pickedColoring.name.trim() == "Indisponível"
            || pickedColoring.name.trim() == "Incolor"
        ) {
            BigDecimal.ZERO
        } else {
            additionalPicked
        }

        return (picked + addPicked - original - addOriginal)
            .setScale(0, RoundingMode.CEILING)
    }

    fun finalPrice(addPrice: Boolean, additional: BigDecimal): BigDecimal {
        // TODO: remove hardcoded string
        val add = if(
            pickedColoring.name.trim() == "Indisponível"
            || pickedColoring.name.trim() == "Incolor"
        ) {
            BigDecimal.ZERO
        } else {
            additional
        }

        return if (addPrice) {
            pickedColoring.price + add
        } else {
            BigDecimal.ZERO
        }
    }
}
