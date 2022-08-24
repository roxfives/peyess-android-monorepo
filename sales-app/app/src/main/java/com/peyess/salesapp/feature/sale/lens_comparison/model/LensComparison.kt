package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import kotlin.math.ceil

data class LensComparison(
    val originalLens: LocalLensEntity,
    val pickedLens: LocalLensEntity,
) {
    val priceDifference = ceil(pickedLens.price - originalLens.price)

}
