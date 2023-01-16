package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.feature.sale.service_order.model.Payment

fun Payment.toSalePaymentDocument(): SalePaymentDocument {
    return SalePaymentDocument(
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