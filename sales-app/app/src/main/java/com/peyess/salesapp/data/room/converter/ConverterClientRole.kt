package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.sale.ClientRole

class ConverterClientRole {
    @TypeConverter
    fun fromName(name: String?): ClientRole? {
        return ClientRole.fromName(name = name)
    }

    @TypeConverter
    fun toName(type: ClientRole?): String? {
        return ClientRole.fromType(type = type)
    }
}