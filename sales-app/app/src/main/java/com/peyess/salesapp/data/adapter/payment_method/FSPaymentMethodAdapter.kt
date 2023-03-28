package com.peyess.salesapp.data.adapter.payment_method

import com.peyess.salesapp.data.model.payment_method.FSPaymentMethod
import com.peyess.salesapp.data.model.payment_method.PaymentMethodDocument


fun FSPaymentMethod.toPaymentMethodDocument(id: String): PaymentMethodDocument {
    return PaymentMethodDocument(
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
    )
}