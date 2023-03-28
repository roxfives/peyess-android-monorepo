package com.peyess.salesapp.utils.time

import com.google.firebase.Timestamp
import java.time.ZonedDateTime

const val sunday = 6
const val saturday = 7

// https://stackoverflow.com/questions/57445264/simpledateformat-returns-wrong-time-from-firestore-timestamp-why
fun ZonedDateTime.toTimestamp(): Timestamp {
    val instant = toInstant()

    return Timestamp(instant.epochSecond, instant.nano)
}

fun ZonedDateTime.isWeekend(): Boolean {
    val day = dayOfWeek.value

    return day == sunday || day == saturday
}

fun ZonedDateTime.nextBusinessDay(): ZonedDateTime {
    val result = ZonedDateTime.from(this)

    return if (isWeekend()) {
        val diffDays = 8 - dayOfWeek.value

        return result.plusDays(diffDays.toLong())
    } else {
        result
    }
}
