package com.peyess.salesapp.screen.sale.lens_comparison.model

data class TreatmentPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val treatment: Treatment = Treatment(),
)
