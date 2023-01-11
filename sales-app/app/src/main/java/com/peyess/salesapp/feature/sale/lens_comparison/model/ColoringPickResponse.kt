package com.peyess.salesapp.feature.sale.lens_comparison.model

data class ColoringPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val coloringPicked: Coloring = Coloring(),
)
