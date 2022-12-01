package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import kotlin.math.ceil

data class ColoringComparison(
    val originalColoring: LocalColoringEntity = LocalColoringEntity(),
    val pickedColoring: LocalColoringEntity = LocalColoringEntity(),
) {
    // TODO: update to price instead of suggestedPrice
    val priceDifference = ceil(pickedColoring.suggestedPrice - originalColoring.suggestedPrice)
    val finalPrice = pickedColoring.suggestedPrice

    fun priceDifference(withOriginal: Boolean, withPicked: Boolean): Double {
        val picked = if (withPicked) { pickedColoring.suggestedPrice } else { 0.0 }
        val suggested = if (withOriginal) { originalColoring.suggestedPrice } else { 0.0 }

        return ceil(picked - suggested)
    }

    fun finalPrice(addPrice: Boolean): Double {
        return if (addPrice) {
            pickedColoring.suggestedPrice
        } else {
            0.0
        }
    }
}
