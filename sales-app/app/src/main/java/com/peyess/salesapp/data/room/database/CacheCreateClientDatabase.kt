package com.peyess.salesapp.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.model.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.room.converter.ConverterSex
import com.peyess.salesapp.data.room.converter.ConverterUri
import com.peyess.salesapp.data.room.converter.ConverterZonedDateTime

@Database(
    entities = [
        CacheCreateClientEntity::class
    ],
    version = 5,
)
@TypeConverters(
    ConverterSex::class,
    ConverterUri::class,
    ConverterZonedDateTime::class,
)
abstract class CacheCreateClientDatabase: RoomDatabase() {
    abstract fun cacheCreateClientDao(): CacheCreateClientDao
}