package com.peyess.salesapp.data.adapter.local_sale.payment

import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity

fun SalePaymentDocument.toSalePaymentEntity(): SalePaymentEntity {
    return SalePaymentEntity(
        id = id,
        soId = saleId,
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
