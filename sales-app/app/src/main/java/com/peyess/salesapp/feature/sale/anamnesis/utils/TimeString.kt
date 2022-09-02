package com.peyess.salesapp.feature.sale.anamnesis.utils

import java.math.BigDecimal

fun toTimeString(totalMinutes: Float): String {
    val time = BigDecimal("$totalMinutes")

    val hoursAndMinutes = time.divideAndRemainder(BigDecimal(60))
    val hours = hoursAndMinutes[0]
    val minutes = hoursAndMinutes[1]

    return "%02dh%02d".format(hours.toInt(), minutes.toInt())
}

fun toTimeString(totalMinutes: Int): String {
    val time = BigDecimal(totalMinutes)

    val hoursAndMinutes = time.divideAndRemainder(BigDecimal(60))
    val hours = hoursAndMinutes[0]
    val minutes = hoursAndMinutes[1]

    return "%02dh%02d".format(hours.toInt(), minutes.toInt())
}