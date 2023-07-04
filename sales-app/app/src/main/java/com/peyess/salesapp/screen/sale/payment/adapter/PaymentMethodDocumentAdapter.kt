package com.peyess.salesapp.screen.sale.payment.adapter

import com.peyess.salesapp.data.model.payment_method.PaymentMethodDocument
import com.peyess.salesapp.feature.payment.model.PaymentMethod

fun PaymentMethodDocument.toPaymentMethod(): PaymentMethod {
    return PaymentMethod(
        id = id,
        type = type,
        priority = priority,
        name = name,
        isEnabled = isEnabled,
        isDownPayment = isDownPayment,
        minPayment = minPayment,
        minSet = minSet,
        hasInstallments = hasInstallments,
        maxInstallments = maxInstallments,
        hasDocumentPicture = hasDocumentPicture,
        hasDocument = hasDocument,
        cardFlags = cardFlags,
        canEditDueDate = canEditDueDate,
        maxDueDate = maxDueDate,
        defaultDueDate = defaultDueDate,
    )
}