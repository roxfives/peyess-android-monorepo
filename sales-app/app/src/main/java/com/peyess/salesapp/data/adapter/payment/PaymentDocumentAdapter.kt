package com.peyess.salesapp.data.adapter.payment

import com.peyess.salesapp.data.model.sale.purchase.FSPayment
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument

fun PaymentDocument.toFSPayment(): FSPayment {
    return FSPayment(
        method = methodName.toName(),
        methodId = methodId,
        amount = amount,
        installments = installments,
        currency = currency,
        document = document,
        financialInst = financialInst.toName(),
        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
        payerUid = payerUid,
        payerDocument = payerDocument,
        payerName = payerName,
    )
}