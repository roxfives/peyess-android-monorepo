package com.peyess.salesapp.data.model.local_sale.payment

import android.net.Uri
import java.time.ZonedDateTime

data class LocalPaymentDocument(
    val uuid: String = "",

    val id: Long = 0L,
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
    val cardFlagName: String = "",
    val cardFlagIcon: Uri = Uri.EMPTY,
    val cardNsu: String = "",

    val daysToDueDate: Int = 0,
    val dueDate: ZonedDateTime = ZonedDateTime.now(),
)
