package com.peyess.salesapp.feature.sale.lens_comparison.model

data class TreatmentPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val treatment: Treatment = Treatment(),
)
