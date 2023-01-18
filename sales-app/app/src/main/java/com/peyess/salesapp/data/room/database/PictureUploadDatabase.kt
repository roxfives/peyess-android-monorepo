package com.peyess.salesapp.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.management_picture_upload.PictureUploadDao
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadEntity
import com.peyess.salesapp.data.room.converter.ConverterUri
import com.peyess.salesapp.data.room.converter.ConverterZonedDateTime

@Database(
    entities = [
        PictureUploadEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(
    ConverterZonedDateTime::class,
    ConverterUri::class,
)
abstract class PictureUploadDatabase: RoomDatabase() {
    abstract fun pictureUploadDao(): PictureUploadDao
}