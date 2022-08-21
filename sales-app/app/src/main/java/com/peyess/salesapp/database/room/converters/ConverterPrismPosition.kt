package com.peyess.salesapp.database.room.converters

import androidx.room.TypeConverter
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import timber.log.Timber

class ConverterPrismPosition {
    @TypeConverter
    fun fromName(name: String?): PrismPosition? {
        Timber.i("Converting $name to ${PrismPosition.toPrism(name)}")

        return PrismPosition.toPrism(name)
    }

    @TypeConverter
    fun toName(type: PrismPosition?): String? {
        Timber.i("Converting $type to ${PrismPosition.toName(type)}")

        return PrismPosition.toName(type)
    }
}