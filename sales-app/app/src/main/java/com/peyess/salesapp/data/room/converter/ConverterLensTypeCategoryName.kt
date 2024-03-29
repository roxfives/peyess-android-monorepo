package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.lens.LensTypeCategoryName

class ConverterLensTypeCategoryName {
    @TypeConverter
    fun fromName(name: String?): LensTypeCategoryName? {
        return LensTypeCategoryName.fromName(name = name)
    }

    @TypeConverter
    fun toName(type: LensTypeCategoryName?): String? {
        return LensTypeCategoryName.fromType(type = type)
    }
}
