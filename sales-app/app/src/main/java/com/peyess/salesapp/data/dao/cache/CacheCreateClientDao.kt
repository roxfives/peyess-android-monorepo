package com.peyess.salesapp.data.dao.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.data.model.cache.CacheCreateClientEntity

@Dao
interface CacheCreateClientDao {
    @Insert(onConflict = REPLACE)
    suspend fun add(client: CacheCreateClientEntity)

    @Update(onConflict = REPLACE)
    suspend fun update(client: CacheCreateClientEntity)

    @Query("DELETE FROM ${CacheCreateClientEntity.tableName} WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM ${CacheCreateClientEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): CacheCreateClientEntity?

    @Query("""
        SELECT * FROM ${CacheCreateClientEntity.tableName} 
        WHERE is_creating = 1
        LIMIT 1
    """)
    suspend fun findCreating(): CacheCreateClientEntity?
}