package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class ConverterZonedDateTime {
    @TypeConverter
    fun fromTimestamp(value: Long?): ZonedDateTime? {
        return value?.let {
            val instant = Instant.ofEpochSecond(value)
            val zone = ZoneId.systemDefault()

            ZonedDateTime.ofInstant(instant, zone)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime?): Long? {
        return date?.toEpochSecond()
    }
}