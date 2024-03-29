package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.client.Sex

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