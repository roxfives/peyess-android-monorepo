package com.peyess.salesapp.data.dao.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheCreateClientDao {
    @Insert(onConflict = REPLACE)
    fun add(client: CacheCreateClientEntity)

    @Update(onConflict = REPLACE)
    fun update(client: CacheCreateClientEntity)

    @Query("DELETE FROM ${CacheCreateClientEntity.tableName} WHERE id = :id")
    fun deleteById(id: String)

    @Query("SELECT * FROM ${CacheCreateClientEntity.tableName} WHERE id = :id")
    fun getById(id: String): Flow<CacheCreateClientEntity?>
}