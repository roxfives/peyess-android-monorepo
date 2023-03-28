package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.products.DiscountCalcMethod

class ConverterOverallDiscountCalcMethod {
    @TypeConverter
    fun fromName(name: String?): DiscountCalcMethod? {
        return DiscountCalcMethod.fromName(method = name)
    }

    @TypeConverter
    fun toName(type: DiscountCalcMethod?): String? {
        return DiscountCalcMethod.toName(method = type)
    }
}