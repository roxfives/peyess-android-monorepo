package com.peyess.salesapp.dao.payment_methods

data class PaymentMethod(
    val id: String = "",

    val type: String = "",
    val priority: Double = 0.0,

    val name: String = "",

    val isEnabled: Boolean = false,

    val isDownPayment: Boolean = false,

    val minPayment: Double = 0.0,
    val minSet: Double = 0.0,

    val hasInstallments: Boolean = false,
    val maxInstallments: Int = 0,

    val hasDocumentPicture: Boolean = false,
    val hasDocument: Boolean = false,

    val cardFlags: List<PaymentCardFlagDesc> = emptyList(),
)

fun FSPaymentMethod.toDocument(id: String): PaymentMethod {
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
    )
}
