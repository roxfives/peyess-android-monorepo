package com.peyess.salesapp.features.pdf.utils

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val emptyIndicator = "-"

fun printValue(value: String): String {
    return value.ifBlank { emptyIndicator }
}

fun printPaymentMethodValue(
    context: Context,
    methodName: String,
    installments: Int,
    forceShowInstallments: Boolean = false,
): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val integerFormat = NumberFormat.getIntegerInstance(currentLocale)
    integerFormat.maximumFractionDigits = 0
    integerFormat.minimumFractionDigits = 0
    integerFormat.minimumIntegerDigits = 2

    return if (!forceShowInstallments && installments <= 1) {
        "$methodName À VISTA"
    } else {
        "${integerFormat.format(installments)}X $methodName"
    }
}

fun printDocument(document: String): String {
    return if (document.length > 1) {
        val documentChunks = document.chunked(3)

        var documentMasked = ""
        (0 until documentChunks.lastIndex - 1).forEach {
            documentMasked += documentChunks[it] + "."
        }

        val lastIndex = (documentChunks.lastIndex - 1).coerceAtLeast(0)
        documentMasked += documentChunks[lastIndex] + "-" + documentChunks.last()

        documentMasked
    } else {
        document
    }
}

fun printDate(date: ZonedDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    return dateFormatter.format(date)

}

fun printHours(date: ZonedDateTime): String {
    val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")

    return hourFormatter.format(date)
}