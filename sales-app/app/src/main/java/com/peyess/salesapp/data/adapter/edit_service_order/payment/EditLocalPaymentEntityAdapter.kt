package com.peyess.salesapp.data.adapter.edit_service_order.payment

import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentEntity
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument

fun EditLocalPaymentEntity.toLocalPaymentDocument(
    clientDocument: String,
    clientName: String,
    clientAddress: String,
): LocalPaymentDocument {
    return LocalPaymentDocument(
        uuid = uuid,

        id = id,
        saleId = saleId,
        clientId = clientId,
        clientDocument = clientDocument,
        clientName = clientName,
        clientAddress = clientAddress,

        methodId = methodId,
        methodName = methodName,

        methodType = methodType,
        value = value,
        installments = installments,
        document = document,

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
        cardNsu = cardNsu,

        daysToDueDate = daysToDueDate,
    )
}
