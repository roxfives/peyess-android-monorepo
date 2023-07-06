package com.peyess.salesapp.data.adapter.payment

import com.peyess.salesapp.data.model.sale.purchase.FSPayment
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
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

        hasLegalId = hasLegalId,
        legalId = legalId,

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,

        payerUid = payerUid,
        payerDocument = payerDocument,
        payerName = payerName,

        dueDateMode = dueDateMode.toName(),
        dueDatePeriod = dueDatePeriod,
        dueDate = dueDate.toTimestamp(),
    )
}