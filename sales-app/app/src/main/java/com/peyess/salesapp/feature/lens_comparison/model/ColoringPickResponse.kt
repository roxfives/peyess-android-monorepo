package com.peyess.salesapp.feature.lens_comparison.model

data class ColoringPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val coloringPicked: Coloring = Coloring(),
)
