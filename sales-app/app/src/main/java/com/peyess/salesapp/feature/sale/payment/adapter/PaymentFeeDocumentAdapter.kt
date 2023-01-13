package com.peyess.salesapp.feature.sale.payment.adapter

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.feature.sale.payment.model.PaymentFee

fun PaymentFeeDocument.toPaymentFee(): PaymentFee {
    return PaymentFee(
        saleId = saleId,
        method = method,
        value = value,
    )
}
