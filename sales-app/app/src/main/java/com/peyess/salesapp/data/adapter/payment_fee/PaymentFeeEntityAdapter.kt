package com.peyess.salesapp.data.adapter.payment_fee

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeEntity

fun PaymentFeeDocument.toPaymentFeeEntity(): PaymentFeeEntity {
    return PaymentFeeEntity(
        saleId = saleId,
        method = method,
        value = value.toDouble(),
    )
}