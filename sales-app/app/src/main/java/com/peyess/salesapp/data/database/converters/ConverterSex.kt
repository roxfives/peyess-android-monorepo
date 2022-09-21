package com.peyess.salesapp.data.database.converters

import androidx.room.TypeConverter
import com.peyess.salesapp.data.model.client.Sex

class ConverterSex {
    @TypeConverter
    fun fromName(name: String?): Sex? {
        return Sex.fromName(name = name)
    }

    @TypeConverter
    fun toName(type: Sex?): String? {
        return Sex.toName(type)
    }
}