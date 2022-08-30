package com.peyess.salesapp.dao.payment_methods

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSPayment(
    @Keep
    @PropertyName("type")
    @JvmField
    val type: String = "",
    @Keep
    @PropertyName("priority")
    @JvmField
    val priority: Double = 0.0,

    @Keep
    @PropertyName("name")
    @JvmField
    val name: String = "",

    @Keep
    @PropertyName("is_enabled")
    @JvmField
    val isEnabled: Boolean = false,

    @Keep
    @PropertyName("is_down_payment")
    @JvmField
    val isDownPayment: Boolean = false,

    @Keep
    @PropertyName("min_payment")
    @JvmField
    val minPayment: Double = 0.0,
    @Keep
    @PropertyName("min_set")
    @JvmField
    val minSet: Double = 0.0,

    @Keep
    @PropertyName("has_installments")
    @JvmField
    val hasInstallments: Boolean = false,
    @Keep
    @PropertyName("max_installments")
    @JvmField
    val maxInstallments: Double = 0.0,

    @Keep
    @PropertyName("has_document_picture")
    @JvmField
    val hasDocumentPicture: Boolean = false,
    @Keep
    @PropertyName("has_document")
    @JvmField
    val hasDocument: Boolean = false,

    @Keep
    @PropertyName("card_flags")
    @JvmField
    val cardFlags: List<PaymentCardFlagDesc> = emptyList(),
)

fun PaymentMethod.toFirestore(): FSPayment {
    return FSPayment(
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
