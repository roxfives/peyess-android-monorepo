package com.peyess.salesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.database.converters.ConverterOffsetDateTime
import com.peyess.salesapp.data.database.converters.ConverterSex
import com.peyess.salesapp.database.room.converters.ConverterUri

@Database(
    entities = [
        CacheCreateClientEntity::class
    ],
    version = 1,
)
@TypeConverters(ConverterSex::class, ConverterUri::class, ConverterOffsetDateTime::class)
abstract class CacheCreateClientDatabase: RoomDatabase() {
    abstract fun cacheCreateClientDao(): CacheCreateClientDao
}