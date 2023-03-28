package com.peyess.salesapp.ui.component.text.utils

import timber.log.Timber
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun percentageDigitsOnlyOrEmpty(value: BigDecimal): String {
    val decimalFormat = NumberFormat.getPercentInstance(Locale.US)
    decimalFormat.minimumFractionDigits = 2
    decimalFormat.maximumFractionDigits = 2
    decimalFormat.minimumIntegerDigits = 1
    decimalFormat.isGroupingUsed = false

    val formattedValue = try {
        decimalFormat.format(value)
    } catch (err: Throwable) {
        Timber.e("Failed to parse: $value", err)
        ""
    }

    val finalValue = formattedValue
        .replace("[^0-9]".toRegex(), "")
        .replace("^[0+(?!\$)]*".toRegex(), "")

    return finalValue
}

fun percentageDigitsOnlyOrEmpty(value: String): String {
    return value
        .replace("[^0-9]".toRegex(), "")
        .replace("^[0+(?!\$)]*".toRegex(), "")
}

fun currencyDigitsOnlyOrEmpty(value: BigDecimal): String {
    val decimalFormat = NumberFormat.getCurrencyInstance(Locale.US)
    decimalFormat.minimumFractionDigits = 2
    decimalFormat.maximumFractionDigits = 2
    decimalFormat.minimumIntegerDigits = 1
    decimalFormat.isGroupingUsed = false

    val formattedValue = try {
        decimalFormat.format(value)
    } catch (err: Throwable) {
        Timber.e("Failed to parse: $value", err)
        ""
    }

    val finalValue = formattedValue
        .replace("[^0-9]".toRegex(), "")
        .replace("^[0+(?!\$)]*".toRegex(), "")

    return finalValue
}

fun currencyDigitsOnlyOrEmpty(value: String): String {
    return value
        .replace("[^0-9]".toRegex(), "")
        .replace("^[0+(?!\$)]*".toRegex(), "")
}