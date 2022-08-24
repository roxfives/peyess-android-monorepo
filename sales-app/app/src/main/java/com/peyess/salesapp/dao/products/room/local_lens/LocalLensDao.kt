package com.peyess.salesapp.dao.products.room.local_lens

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalLensDao {
    @Insert
    fun add(lensEntity: LocalLensEntity)

    @RawQuery(observedEntities = [LocalLensEntity::class])
    fun getFilteredLenses(query: SimpleSQLiteQuery): PagingSource<Int, LocalLensEntity>
//
//    @Query("SELECT * FROM ${LocalLensEntity.tableName} WHERE")
//    fun getById(lensId: String): Flow<LocalLensEntity?>
}