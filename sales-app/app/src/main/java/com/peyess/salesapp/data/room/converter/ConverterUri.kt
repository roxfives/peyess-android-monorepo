package com.peyess.salesapp.data.room.converter

import android.net.Uri
import androidx.room.TypeConverter

class ConverterUri {
    @TypeConverter
    fun fromUri(value: Uri?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun stringToUri(uri: String?): Uri? {
        return Uri.parse(uri)
    }
}