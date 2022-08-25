package com.peyess.salesapp.feature.sale.lens_comparison.model

import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import kotlin.math.ceil

data class TreatmentComparison(
    val originalTreatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val pickedTreatment: LocalTreatmentEntity = LocalTreatmentEntity(),
) {
    val priceDifference = ceil(pickedTreatment.price - originalTreatment.price)
    val finalPrice = pickedTreatment.price
}
