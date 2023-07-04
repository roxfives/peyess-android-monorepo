package com.peyess.salesapp.data.model.payment_method

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSPaymentMethod(
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
    val maxInstallments: Int = 1,

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

    @Keep
    @PropertyName("can_edit_due_date")
    @JvmField
    val canEditDueDate: Boolean = false,
    @Keep
    @PropertyName("max_due_date")
    @JvmField
    val maxDueDate: Int = 0,
    @Keep
    @PropertyName("default_due_date")
    @JvmField
    val defaultDueDate: Int = 0,

    @Keep
    @JvmField
    @PropertyName("doc_version")
    val doc_version: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val is_editable: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)

