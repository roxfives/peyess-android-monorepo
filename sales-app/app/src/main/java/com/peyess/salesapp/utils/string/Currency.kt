package com.peyess.salesapp.utils.string

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

private fun formatNumberAsPrice(
    value: Number,
    minimumFractionDigits: Int = 2,
    maximumFractionDigits: Int = 2,
    minimumIntegerDigits: Int = 3,
): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    currencyFormatter.minimumFractionDigits = minimumFractionDigits
    currencyFormatter.maximumFractionDigits = maximumFractionDigits
    currencyFormatter.minimumIntegerDigits = minimumIntegerDigits

    return currencyFormatter.format(value)
}

fun formatAsPrice(
    value: BigDecimal,
    minimumFractionDigits: Int = 2,
    maximumFractionDigits: Int = 2,
    minimumIntegerDigits: Int = 3,
): String {
    return formatNumberAsPrice(
        value = value,
        minimumFractionDigits = minimumFractionDigits,
        maximumFractionDigits = maximumFractionDigits,
        minimumIntegerDigits = minimumIntegerDigits,
    )
}

fun formatAsPrice(
    value: Double,
    minimumFractionDigits: Int = 2,
    maximumFractionDigits: Int = 2,
    minimumIntegerDigits: Int = 3,
): String {
    return formatNumberAsPrice(
        value = value,
        minimumFractionDigits = minimumFractionDigits,
        maximumFractionDigits = maximumFractionDigits,
        minimumIntegerDigits = minimumIntegerDigits,
    )
}

fun formatAsPrice(
    value: Int,
    minimumFractionDigits: Int = 2,
    maximumFractionDigits: Int = 2,
    minimumIntegerDigits: Int = 3,
): String {
    return formatNumberAsPrice(
        value = value,
        minimumFractionDigits = minimumFractionDigits,
        maximumFractionDigits = maximumFractionDigits,
        minimumIntegerDigits = minimumIntegerDigits,
    )
}
