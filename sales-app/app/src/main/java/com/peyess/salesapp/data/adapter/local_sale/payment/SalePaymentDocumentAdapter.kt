package com.peyess.salesapp.data.adapter.local_sale.payment

import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentEntity

fun LocalPaymentDocument.toLocalPaymentEntity(): LocalPaymentEntity {
    return LocalPaymentEntity(
        id = id,
        uuid = uuid,
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

        hasLegalId = hasLegalId,
        legalId = legalId,

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,

        dueDateMode = dueDateMode,
        dueDatePeriod = dueDatePeriod,
        dueDate = dueDate,
    )
}
