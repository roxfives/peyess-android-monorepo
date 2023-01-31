package com.peyess.salesapp.feature.sale.service_order.utils.adapter

import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType

fun SalePaymentDocument.toPaymentDocument(): PaymentDocument {
    return PaymentDocument(
        methodName = PaymentMethodType.fromName(methodType),
        methodId = methodId,
        amount = value,
        installments = installments,
        document = document,
        financialInst = FinancialInstitutionType.fromName("bank"),
        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon.toString(),
        payerUid = clientId,
        payerDocument = clientDocument,
        payerName = clientName,
    )
}