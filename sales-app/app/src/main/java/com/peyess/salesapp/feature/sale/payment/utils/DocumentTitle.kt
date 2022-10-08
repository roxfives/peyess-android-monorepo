package com.peyess.salesapp.feature.sale.payment.utils

import androidx.annotation.StringRes
import com.peyess.salesapp.R
import com.peyess.salesapp.typing.sale.PaymentMethodType

@StringRes
fun methodDocumentTitle(type: String?): Int {
    val methodType = PaymentMethodType.fromName(type ?: "")

    return when (methodType) {
        PaymentMethodType.BankCheck -> R.string.payment_document_check
        PaymentMethodType.BankDeposit -> R.string.payment_document_deposit
        else -> R.string.empty_string
    }
}

@StringRes
fun methodDocumentPlaceholder(type: String?): Int {
    val methodType = PaymentMethodType.fromName(type ?: "")

    return when (methodType) {
        PaymentMethodType.BankCheck -> R.string.payment_document_check
        PaymentMethodType.BankDeposit -> R.string.payment_document_deposit
        else -> R.string.empty_string
    }
}