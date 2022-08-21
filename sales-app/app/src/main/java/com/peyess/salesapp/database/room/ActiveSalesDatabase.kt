package com.peyess.salesapp.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.database.room.converters.ConverterEye
import com.peyess.salesapp.database.room.converters.ConverterFramesType
import com.peyess.salesapp.database.room.converters.ConverterLensTypeCategoryName
import com.peyess.salesapp.database.room.converters.ConverterLocalDate
import com.peyess.salesapp.database.room.converters.ConverterPrismPosition
import com.peyess.salesapp.database.room.converters.ConverterUri

@Database(
    entities = [
        ActiveSalesEntity::class,
        ActiveSOEntity::class,
        PrescriptionPictureEntity::class,
        PrescriptionDataEntity::class,
        FramesEntity::class,
        PositioningEntity::class,
    ],
    version = 16,
)
@TypeConverters(
    ConverterLocalDate::class,
    ConverterUri::class,
    ConverterLensTypeCategoryName::class,
    ConverterPrismPosition::class,
    ConverterFramesType::class,
    ConverterEye::class,
)
abstract class ActiveSalesDatabase: RoomDatabase() {
    abstract fun activeSalesDao(): ActiveSalesDao

    abstract fun activeSODao(): ActiveSODao

    abstract fun prescriptionPictureDao(): PrescriptionPictureDao

    abstract fun prescriptionDataDao(): PrescriptionDataDao

    abstract fun framesDataDao(): FramesDataDao

    abstract fun positioningDao(): PositioningDao
}