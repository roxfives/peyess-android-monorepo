package com.peyess.salesapp.data.adapter.purchase.fee

import com.peyess.salesapp.data.model.sale.purchase.fee.FSFeeDescription
import com.peyess.salesapp.data.model.sale.purchase.fee.FeeDescriptionDocument
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.math.BigDecimal

fun FSFeeDescription.toFeeDescriptionDocument(): FeeDescriptionDocument {
    return FeeDescriptionDocument(
        method = PaymentFeeCalcMethod.fromName(method),
        value = value.toBigDecimal(),
    )
}
