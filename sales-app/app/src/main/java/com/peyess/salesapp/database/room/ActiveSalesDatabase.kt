package com.peyess.salesapp.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity

@Database(
    entities = [
        ActiveSalesEntity::class,
        ActiveSOEntity::class,
    ],
    version = 1,
)
abstract class ActiveSalesDatabase: RoomDatabase() {
    abstract fun activeSalesDao(): ActiveSalesDao

    abstract fun activeSODao(): ActiveSODao
}