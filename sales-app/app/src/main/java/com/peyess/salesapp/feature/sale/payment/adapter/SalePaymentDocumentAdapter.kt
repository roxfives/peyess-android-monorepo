package com.peyess.salesapp.feature.sale.payment.adapter

import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.feature.sale.payment.model.Payment

fun SalePaymentDocument.toPayment(): Payment {
    return Payment(
        id = id,
        saleId = saleId,
        clientId = clientId,
        clientDocument = clientDocument,
        clientName = clientName,
        clientAddress = clientAddress,
        clientPicture = clientPicture,
        methodId = methodId,
        methodName = methodName,
        methodType = methodType,
        value = value,
        installments = installments,
        document = document,
        docPicture = docPicture,
        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
    )
}
