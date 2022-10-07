package com.peyess.salesapp.utils.time

import com.google.firebase.Timestamp
import java.time.ZonedDateTime

// https://stackoverflow.com/questions/57445264/simpledateformat-returns-wrong-time-from-firestore-timestamp-why
fun ZonedDateTime.toTimestamp(): Timestamp {
    val instant = toInstant()

    return Timestamp(instant.epochSecond, instant.nano)
}