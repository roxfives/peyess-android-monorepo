package com.peyess.salesapp.feature.service_order.model

import android.net.Uri
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import java.time.ZonedDateTime

data class Payment(
    val id: Long = 0L,
    val uuid: String = "",
    val saleId: String = "",

    val clientId: String = "",
    val clientDocument: String = "",
    val clientName: String = "",
    val clientAddress: String = "",

    val methodId: String = "",
    val methodName: String = "",
    val methodType: String = "unknown",

    val value: Double = 0.0,
    val installments: Int = 1,
    val document: String = "",

    val hasLegalId: Boolean = false,
    val legalId: String = "",

    val cardFlagName: String = "",
    val cardFlagIcon: Uri = Uri.EMPTY,

    val dueDateMode: PaymentDueDateMode = PaymentDueDateMode.None,
    val dueDatePeriod: Int = 0,
    val dueDate: ZonedDateTime = ZonedDateTime.now(),
)
