package com.peyess.salesapp.data.dao.local_sale.positioning

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.feature.sale.frames.landing.state.Eye
import kotlinx.coroutines.flow.Flow

@Dao
interface PositioningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(positioningEntity: PositioningEntity)

    @Query("SELECT * FROM ${PositioningEntity.tableName} as p WHERE p.so_id = :soId AND p.eye = :eye ")
    fun getById(soId: String, eye: Eye): Flow<PositioningEntity?>

    @Query("SELECT * FROM ${PositioningEntity.tableName} as p WHERE p.so_id = :soId AND p.eye = :eye ")
    suspend fun getPositioningForServiceOrder(soId: String, eye: Eye): PositioningEntity?
}
