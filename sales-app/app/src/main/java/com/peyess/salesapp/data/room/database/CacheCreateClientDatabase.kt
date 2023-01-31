package com.peyess.salesapp.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.room.converter.ConverterOffsetDateTime
import com.peyess.salesapp.data.room.converter.ConverterSex
import com.peyess.salesapp.data.room.converter.ConverterUri

@Database(
    entities = [
        CacheCreateClientEntity::class
    ],
    version = 3,
)
@TypeConverters(ConverterSex::class, ConverterUri::class, ConverterOffsetDateTime::class)
abstract class CacheCreateClientDatabase: RoomDatabase() {
    abstract fun cacheCreateClientDao(): CacheCreateClientDao
}