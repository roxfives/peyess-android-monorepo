package com.peyess.salesapp.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.database.room.converters.ConverterLocalDate
import com.peyess.salesapp.database.room.converters.ConverterUri

@Database(
    entities = [
        ActiveSalesEntity::class,
        ActiveSOEntity::class,
        PrescriptionPictureEntity::class,
    ],
    version = 3,
)
@TypeConverters(ConverterLocalDate::class, ConverterUri::class)
abstract class ActiveSalesDatabase: RoomDatabase() {
    abstract fun activeSalesDao(): ActiveSalesDao

    abstract fun activeSODao(): ActiveSODao

    abstract fun prescriptionPictureDao(): PrescriptionPictureDao
}