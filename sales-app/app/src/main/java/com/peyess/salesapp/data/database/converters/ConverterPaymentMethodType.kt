package com.peyess.salesapp.data.database.converters

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.sale.PaymentMethodType
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

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