package com.peyess.salesapp.screen.sale.fee.adapter

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.feature.payment_fee.model.PaymentFee
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

fun PaymentFeeDocument.toPaymentFee(): PaymentFee {
    return when (method) {
        PaymentFeeCalcMethod.Percentage ->
            PaymentFee(
                method = method,
                percentValue = value.toDouble(),
            )

        PaymentFeeCalcMethod.Whole ->
            PaymentFee(
                method = method,
                wholeValue = value.toDouble(),
            )

        PaymentFeeCalcMethod.None ->
            PaymentFee(method = method)
    }
}

fun PaymentFee.toPaymentFeeDocument(saleId: String): PaymentFeeDocument {
    return PaymentFeeDocument(
        saleId = saleId,
        method = method,
        value = value.toBigDecimal(),
    )
}