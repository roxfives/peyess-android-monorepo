package com.peyess.salesapp.utils.time

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

// https://stackoverflow.com/questions/57445264/simpledateformat-returns-wrong-time-from-firestore-timestamp-why
fun Timestamp.toZonedDateTime(): ZonedDateTime {
    val instant = Instant.ofEpochSecond(seconds , nanoseconds.toLong())
    val zone = ZoneId.systemDefault()

    return instant.atZone(zone)
}