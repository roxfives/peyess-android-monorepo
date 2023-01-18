package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class ConverterLocalDate {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}