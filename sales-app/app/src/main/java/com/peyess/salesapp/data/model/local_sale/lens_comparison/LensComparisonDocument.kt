package com.peyess.salesapp.data.model.local_sale.lens_comparison

data class LensComparisonDocument(
    val id: Int = 0,
    val soId: String = "",

    val originalLensId: String = "",
    val originalColoringId: String = "",
    val originalTreatmentId: String = "",
    val comparisonLensId: String = "",
    val comparisonColoringId: String = "",
    val comparisonTreatmentId: String = "",
)
