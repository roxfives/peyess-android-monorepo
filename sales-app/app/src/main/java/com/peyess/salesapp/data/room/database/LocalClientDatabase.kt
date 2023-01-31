package com.peyess.salesapp.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.local_client.LocalClientDao
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.data.model.local_client.LocalClientStatusEntity
import com.peyess.salesapp.data.room.converter.ConverterSex
import com.peyess.salesapp.data.room.converter.ConverterUri
import com.peyess.salesapp.data.room.converter.ConverterZonedDateTime

@Database(
    entities = [
        LocalClientStatusEntity::class,
        LocalClientEntity::class,
    ],
    version = 4,
)
@TypeConverters(
    ConverterSex::class,
    ConverterUri::class,
    ConverterZonedDateTime::class,
)
abstract class LocalClientDatabase: RoomDatabase() {
    abstract fun localClientDao(): LocalClientDao
}