package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.feature.sale.service_order.model.Payment

fun SalePaymentDocument.toPayment(): Payment {
    return Payment(
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
    )
}
