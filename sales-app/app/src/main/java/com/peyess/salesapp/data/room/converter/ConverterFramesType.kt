package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.frames.FramesType
import timber.log.Timber

class ConverterFramesType {
    @TypeConverter
    fun fromName(name: String?): FramesType? {
        Timber.i("Converting $name to ${FramesType.toFramesType(name ?: "")}")

        return FramesType.toFramesType(name ?: "")
    }

    @TypeConverter
    fun toName(type: FramesType?): String? {
        Timber.i("Converting $type to ${FramesType.toName(type ?: FramesType.None)}")

        return FramesType.toName(type ?: FramesType.None)
    }
}