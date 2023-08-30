package com.peyess.salesapp.data.adapter.edit_service_order.payment_discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.edit_service_order.payment_discount.EditOverallDiscountEntity
import com.peyess.salesapp.utils.extentions.roundToDouble
import java.math.RoundingMode

fun OverallDiscountDocument.toEditOverallDiscountEntity(
    roundValue: Boolean = false,
): EditOverallDiscountEntity {
    return EditOverallDiscountEntity(
        saleId = saleId,
        method = discountMethod,
        value = overallDiscountValue.roundToDouble(roundValue),
    )
}
