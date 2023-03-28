package com.peyess.salesapp.utils.products

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod

data class ProductSet(
    val lens: LocalLensEntity = LocalLensEntity(),
    val coloring: LocalColoringEntity = LocalColoringEntity(),
    val treatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val frames: FramesEntity = FramesEntity(),
    val discount: OverallDiscountDocument = OverallDiscountDocument(),
) {
    fun calculateDiscount(totalToPay: Double): Double {
        return when (discount.discountMethod) {
            DiscountCalcMethod.None -> totalToPay
            DiscountCalcMethod.Percentage -> totalToPay * (1.0 - discount.overallDiscountValue)
            DiscountCalcMethod.Whole -> totalToPay - discount.overallDiscountValue
        }
    }
}
