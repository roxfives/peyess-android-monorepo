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
}
