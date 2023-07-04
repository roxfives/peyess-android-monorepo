package com.peyess.salesapp.screen.sale.payment.adapter

import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.feature.payment.model.Payment
import java.time.LocalTime
import java.time.ZonedDateTime

fun Payment.toLocalPaymentDocument(): LocalPaymentDocument {
    val dueDate = ZonedDateTime.now().plusDays(daysToDueDate.toLong())

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