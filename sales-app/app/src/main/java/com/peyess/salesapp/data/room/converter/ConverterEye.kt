package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.general.Eye
import timber.log.Timber

class ConverterEye {
    @TypeConverter
    fun fromName(name: String?): Eye? {
        Timber.i("Converting $name to ${Eye.toEye(name)}")

        return Eye.toEye(name)
    }

    @TypeConverter
    fun toName(type: Eye?): String? {
        Timber.i("Converting $type to ${Eye.toName(type)}")

        return Eye.toName(type)
    }
}