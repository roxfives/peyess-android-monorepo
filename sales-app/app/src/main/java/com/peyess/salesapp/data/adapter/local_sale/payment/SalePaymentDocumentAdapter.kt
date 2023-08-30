package com.peyess.salesapp.data.adapter.local_sale.payment

import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentEntity
import java.math.RoundingMode

fun LocalPaymentDocument.toLocalPaymentEntity(roundPayment: Boolean = true): LocalPaymentEntity {
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
        value = if (roundPayment) {
            value.setScale(2, RoundingMode.HALF_EVEN).toDouble()
        } else {
            value.toDouble()
        },
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
