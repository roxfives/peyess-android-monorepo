package com.peyess.salesapp.data.room.converter

import androidx.room.TypeConverter
import com.peyess.salesapp.typing.sale.PaymentDueDateMode

class ConverterPaymentDueDateMode {
    @TypeConverter
    fun fromName(name: String?): PaymentDueDateMode? {
        return PaymentDueDateMode.fromName(value = name ?: "")
    }

    @TypeConverter
    fun toName(type: PaymentDueDateMode?): String? {
        return PaymentDueDateMode.toName(value = type ?: PaymentDueDateMode.None)
    }
}