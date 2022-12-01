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

    fun priceDifference(withOriginal: Boolean, withPicked: Boolean): Double {
        val picked = if (withPicked) { pickedTreatment.suggestedPrice } else { 0.0 }
        val suggested = if (withOriginal) { originalTreatment.suggestedPrice } else { 0.0 }

        return ceil(picked - suggested)
    }

    fun finalPrice(addPrice: Boolean): Double {
        return if (addPrice) {
            pickedTreatment.suggestedPrice
        } else {
            0.0
        }
    }
}
