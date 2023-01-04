package com.peyess.salesapp.utils.time

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun LocalDate.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.of(this, LocalTime.MIDNIGHT, ZoneId.systemDefault())
}