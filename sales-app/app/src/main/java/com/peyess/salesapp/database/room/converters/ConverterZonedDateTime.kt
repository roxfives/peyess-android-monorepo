package com.peyess.salesapp.database.room.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDate.ofEpochDay
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