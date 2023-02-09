package com.peyess.salesapp.data.adapter.edit_service_order.payment

import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentEntity
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument

fun LocalPaymentDocument.toEditLocalPaymentEntity(): EditLocalPaymentEntity {
    return EditLocalPaymentEntity(
        id = id,
        saleId = saleId,
        clientId = clientId,
        methodId = methodId,
        methodName = methodName,
        methodType = methodType,
        value = value,
        installments = installments,
        document = document,
        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
    )
}
