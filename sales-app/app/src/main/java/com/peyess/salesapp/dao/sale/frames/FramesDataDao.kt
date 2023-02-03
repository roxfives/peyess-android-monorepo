package com.peyess.salesapp.dao.sale.frames

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FramesDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(framesEntity: FramesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(framesEntity: FramesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFrames(framesEntity: FramesEntity)

    @Query("SELECT * FROM ${FramesEntity.tableName} as p WHERE p.so_id = :soId ")
    fun getById(soId: String): Flow<FramesEntity?>

    @Query("SELECT * FROM ${FramesEntity.tableName} as p WHERE p.so_id = :soId ")
    suspend fun getFramesForServiceOrder(soId: String): FramesEntity?
}