package com.peyess.salesapp.data.adapter.payment

import com.peyess.salesapp.data.model.sale.purchase.FSPayment
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType
import com.peyess.salesapp.utils.time.toZonedDateTime
import java.time.ZonedDateTime

fun FSPayment.toPaymentDocument(): PaymentDocument {
    return PaymentDocument(
        uuid = uuid,

        methodName = PaymentMethodType.fromName(method),
        methodId = methodId,
        amount = amount,
        installments = installments,
        currency = currency,
        document = document,
        financialInst = FinancialInstitutionType.fromName(financialInst),

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,
        cardNsu = cardNsu,

        payerUid = payerUid,
        payerDocument = payerDocument,
        payerName = payerName,

        dueDate = dueDate.toZonedDateTime(),
        daysToDueDate = daysToDueDate,
    )
}