package com.peyess.salesapp.data.adapter.edit_service_order.payment_fee

import com.peyess.salesapp.data.model.edit_service_order.payment_fee.EditPaymentFeeEntity
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument

fun EditPaymentFeeEntity.toPaymentFeeDocument(): PaymentFeeDocument {
    return PaymentFeeDocument(
        saleId = saleId,
        method = method,
        value = value,
    )
}
