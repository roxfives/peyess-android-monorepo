package com.peyess.salesapp.screen.sale.fee.model

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

fun PaymentFeeDocument.toPaymentFee(): PaymentFee {
    return when (method) {
        PaymentFeeCalcMethod.Percentage ->
            PaymentFee(
                method = method,
                percentValue = value,
            )

        PaymentFeeCalcMethod.Whole ->
            PaymentFee(
                method = method,
                wholeValue = value,
            )

        PaymentFeeCalcMethod.None ->
            PaymentFee(method = method)
    }
}

fun PaymentFee.toPaymentFeeDocument(saleId: String): PaymentFeeDocument {
    return PaymentFeeDocument(
        saleId = saleId,
        method = method,
        value = value
    )
}