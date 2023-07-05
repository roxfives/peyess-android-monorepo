package com.peyess.salesapp.data.model.payment_method

import com.peyess.salesapp.typing.sale.PaymentDueDateMode

data class PaymentMethodDocument(
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

    val dueDateMode: PaymentDueDateMode = PaymentDueDateMode.None,
    val dueDateDefault: Int = 0,
    val dueDateMax: Int = 0,
)
