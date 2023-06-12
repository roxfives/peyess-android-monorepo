package com.peyess.salesapp.feature.service_order.model

import android.net.Uri

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
    val cardFlagName: String = "",
    val cardFlagIcon: Uri = Uri.EMPTY,
    val cardNsu: String = "",
)
