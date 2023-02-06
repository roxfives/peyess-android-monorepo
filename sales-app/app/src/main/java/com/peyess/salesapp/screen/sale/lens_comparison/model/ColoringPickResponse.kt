package com.peyess.salesapp.screen.sale.lens_comparison.model

data class ColoringPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val coloringPicked: Coloring = Coloring(),
)
