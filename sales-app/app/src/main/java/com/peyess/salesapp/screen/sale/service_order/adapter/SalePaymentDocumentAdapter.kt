package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.feature.service_order.model.Payment
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType

fun LocalPaymentDocument.toPayment(): Payment {
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

        dueDateMode = dueDateMode,
        dueDatePeriod = dueDatePeriod,
        dueDate = dueDate,
    )
}

fun LocalPaymentDocument.toPaymentDocument(): PaymentDocument {
    return PaymentDocument(
        uuid = uuid,

        methodName = PaymentMethodType.fromName(methodType),
        methodId = methodId,
        amount = value,
        installments = installments,
        document = document,
        financialInst = FinancialInstitutionType.fromName("bank"),

        hasLegalId = hasLegalId,
        legalId = legalId,

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon.toString(),

        payerUid = clientId,
        payerDocument = clientDocument,
        payerName = clientName,

        dueDateMode = dueDateMode,
        dueDatePeriod = dueDatePeriod,
        dueDate = dueDate,
    )
}
