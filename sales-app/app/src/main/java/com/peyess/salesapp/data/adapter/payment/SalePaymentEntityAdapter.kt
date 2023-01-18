package com.peyess.salesapp.data.adapter.payment

import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.model.sale.purchase.FSPayment
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType
//
//fun SalePaymentEntity.toFSPayment(): FSPayment {
//    return FSPayment(
//        method = methodName,
//        methodId = methodId,
//        amount = value,
//        installments = installments,
//
//        // TODO: get currency from local device/app settings
//        currency = "BRL",
//        document = document,
//
//        documentPicture = "",
//        financialInst = "",
//
//        cardFlagName = "",
//        cardFlagIcon = "",
//
//        payerUid = clientId,
//        payerDocument = clientId,
//        payerName = clientName,
//        payerPicture = clientPicture.toString(),
//    )
//}

//fun SalePaymentEntity.toPaymentDocument(): PaymentDocument {
//    return PaymentDocument(
//        methodName = PaymentMethodType.fromName(methodType),
//        methodId = methodId,
//        amount = value,
//        installments = installments,
//
//        // TODO: get currency from local device/app settings
//        currency = "BRL",
//        document = document,
//
//        documentPicture = "",
//        financialInst = FinancialInstitutionType.fromName(""),
//
//        cardFlagName = cardFlagName,
//        cardFlagIcon = cardFlagIcon.toString(),
//
//        payerUid = clientId,
//        payerDocument = clientDocument,
//        payerName = clientName,
//        payerPicture = clientPicture.toString(),
//    )
//}