package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import java.time.ZonedDateTime

@Keep
@IgnoreExtraProperties
data class FSPayment(
    @Keep
    @JvmField
    @PropertyName("uuid")
    val uuid: String = "",

    @Keep
    @JvmField
    @PropertyName("method")
    val method: String = "", // PaymentMethodType
    @Keep
    @JvmField
    @PropertyName("method_id")
    val methodId: String = "",
    @Keep
    @JvmField
    @PropertyName("amount")
    val amount: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("installments")
    val installments: Int = 0,
    @Keep
    @JvmField
    @PropertyName("currency")
    val currency: String = "BRL", // Currency
    @Keep
    @JvmField
    @PropertyName("document")
    val document: String = "",
    @Keep
    @JvmField
    @PropertyName("financial_inst")
    val financialInst: String = FinancialInstitutionType.None.toName(), // FinantialInstitutionType

    @Keep
    @JvmField
    @PropertyName("card_nsu")
    val cardNsu: String = "",
    @Keep
    @JvmField
    @PropertyName("card_flag_name")
    val cardFlagName: String = "",
    @Keep
    @JvmField
    @PropertyName("card_flag_icon")
    val cardFlagIcon: String = "",

    @Keep
    @JvmField
    @PropertyName("payer_uid")
    val payerUid: String = "",
    @Keep
    @JvmField
    @PropertyName("payer_document")
    val payerDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("payer_name")
    val payerName: String = "",
    
    @Keep
    @JvmField
    @PropertyName("due_date")
    val dueDate: Timestamp = Timestamp.now(),
)
