package com.peyess.salesapp.feature.payment.utils

import androidx.annotation.StringRes
import com.peyess.salesapp.R
import com.peyess.salesapp.typing.sale.PaymentMethodType

@StringRes
fun methodDocumentTitle(type: String?): Int {
    val methodType = PaymentMethodType.fromName(type ?: "")

    return when (methodType) {
        PaymentMethodType.BankCheck -> R.string.payment_document_check
        PaymentMethodType.BankDeposit -> R.string.payment_document_deposit
        PaymentMethodType.Pix -> R.string.payment_document_pix
        else -> R.string.empty_string
    }
}

@StringRes
fun methodDocumentPlaceholder(type: String?): Int {
    val methodType = PaymentMethodType.fromName(type ?: "")

    return when (methodType) {
        PaymentMethodType.BankCheck -> R.string.payment_document_check
        PaymentMethodType.BankDeposit -> R.string.payment_document_deposit
        PaymentMethodType.Pix -> R.string.payment_document_pix
        else -> R.string.empty_string
    }
}

@StringRes
fun legalIdTitle(type: String?): Int {
    val methodType = PaymentMethodType.fromName(type ?: "")

    return when (methodType) {
        PaymentMethodType.Credit,
        PaymentMethodType.CreditFull,
        PaymentMethodType.Debit -> R.string.payment_legal_id_card
        else -> R.string.empty_string
    }
}

@StringRes
fun legalIdPlaceholder(type: String?): Int {
    val methodType = PaymentMethodType.fromName(type ?: "")

    return when (methodType) {
        PaymentMethodType.Credit,
        PaymentMethodType.CreditFull,
        PaymentMethodType.Debit -> R.string.payment_legal_id_card
        else -> R.string.empty_string
    }
}