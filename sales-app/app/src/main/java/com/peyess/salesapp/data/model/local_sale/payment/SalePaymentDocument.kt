package com.peyess.salesapp.data.model.local_sale.payment

import android.net.Uri

data class SalePaymentDocument(
    val id: Long = 0L,
    val saleId: String = "",

    val clientId: String = "",
    val clientDocument: String = "",
    val clientName: String = "",
    val clientAddress: String = "",
    val clientPicture: Uri = Uri.EMPTY,

    val methodId: String = "",
    val methodName: String = "",
    val methodType: String = "unknown",

    val value: Double = 0.0,
    val installments: Int = 1,
    val document: String = "",
    val docPicture: Uri = Uri.EMPTY,
    val cardFlagName: String = "",
    val cardFlagIcon: Uri = Uri.EMPTY,
)