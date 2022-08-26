package com.peyess.salesapp.database.room.converters

import androidx.room.TypeConverter
import com.peyess.salesapp.dao.client.room.ClientRole

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