package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import kotlin.math.ceil

data class ColoringComparison(
    val originalColoring: LocalColoringEntity = LocalColoringEntity(),
    val pickedColoring: LocalColoringEntity = LocalColoringEntity(),
) {
    val priceDifference = ceil(pickedColoring.price - originalColoring.price)
    val finalPrice = pickedColoring.price

}
