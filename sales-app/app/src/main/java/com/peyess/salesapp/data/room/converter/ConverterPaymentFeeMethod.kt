package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

class ConverterPaymentFeeMethod {
    @TypeConverter
    fun fromName(name: String?): PaymentFeeCalcMethod? {
        return PaymentFeeCalcMethod.fromName(method = name)
    }

    @TypeConverter
    fun toName(type: PaymentFeeCalcMethod?): String? {
        return PaymentFeeCalcMethod.toName(method = type)
    }
}