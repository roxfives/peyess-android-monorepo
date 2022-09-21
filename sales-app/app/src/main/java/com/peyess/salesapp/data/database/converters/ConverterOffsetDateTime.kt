package com.peyess.salesapp.data.database.converters

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ConverterOffsetDateTime {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}