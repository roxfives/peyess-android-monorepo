package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.sale.PaymentMethodType

class ConverterPaymentMethodType {

    @TypeConverter
    fun toPaymentMethodType(value: String?): PaymentMethodType? {
        return value?.let {
            return PaymentMethodType.fromName(value)
        }
    }

    @TypeConverter
    fun fromPaymentMethodType(method: PaymentMethodType?): String? {
        return method?.toName()
    }
}