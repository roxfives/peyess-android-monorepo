package com.peyess.salesapp.data.adapter.payment

import com.peyess.salesapp.data.model.sale.purchase.FSPayment
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType

fun FSPayment.toPaymentDocument(): PaymentDocument {
    return PaymentDocument(
        methodName = PaymentMethodType.fromName(method),
        methodId = methodId,
        amount = amount,
        installments = installments,
        currency = currency,
        document = document,
        documentPicture = documentPicture,
        financialInst = FinancialInstitutionType.fromName(financialInst),
        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
        payerUid = payerUid,
        payerDocument = payerDocument,
        payerName = payerName,
        payerPicture = payerPicture,
    )
}