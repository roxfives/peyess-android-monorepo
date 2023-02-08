package com.peyess.salesapp.data.adapter.edit_service_order.payment

import com.peyess.salesapp.data.model.edit_service_order.payment.EditSalePaymentDBView
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument

fun EditSalePaymentDBView.toSalePaymentDocument(): SalePaymentDocument {
    return SalePaymentDocument(
        id = id,
        saleId = saleId,
        clientId = clientId,
        clientDocument = clientDocument,
        clientName = clientName,
        clientAddress = "$clientCity, $clientState",
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
