package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import kotlin.math.ceil

data class TreatmentComparison(
    val originalTreatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val pickedTreatment: LocalTreatmentEntity = LocalTreatmentEntity(),
) {
    // TODO: update to price instead of suggestedPrice
    val priceDifference = ceil(pickedTreatment.suggestedPrice - originalTreatment.suggestedPrice)
    val finalPrice = pickedTreatment.suggestedPrice
}
