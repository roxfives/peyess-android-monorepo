package com.peyess.salesapp.dao.sale.frames

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FramesDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(framesEntity: FramesEntity)

    @Update
    fun update(framesEntity: FramesEntity)

    @Update
    suspend fun updateFrames(framesEntity: FramesEntity)

    @Query("""
        UPDATE ${FramesEntity.tableName}
        SET is_new = :isNew
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateFramesNew(serviceOrderId: String, isNew: Int)

    @Query("SELECT * FROM ${FramesEntity.tableName} as p WHERE p.so_id = :soId ")
    fun getById(soId: String): Flow<FramesEntity?>

    @Query("SELECT * FROM ${FramesEntity.tableName} as p WHERE p.so_id = :soId ")
    suspend fun getFramesForServiceOrder(soId: String): FramesEntity?
}