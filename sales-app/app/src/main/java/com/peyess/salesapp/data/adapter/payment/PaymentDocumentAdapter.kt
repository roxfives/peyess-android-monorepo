package com.peyess.salesapp.data.adapter.payment

import com.peyess.salesapp.data.model.sale.purchase.FSPayment
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PaymentDocument.toFSPayment(): FSPayment {
    return FSPayment(
        uuid = uuid,

        method = methodName.toName(),
        methodId = methodId,
        amount = amount,
        installments = installments,
        currency = currency,
        document = document,
        financialInst = financialInst.toName(),

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
        cardNsu = cardNsu,

        payerUid = payerUid,
        payerDocument = payerDocument,
        payerName = payerName,

        dueDate = dueDate.toTimestamp(),
        daysToDueDate = daysToDueDate,
    )
}