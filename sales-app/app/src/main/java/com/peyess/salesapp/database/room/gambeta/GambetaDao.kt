package com.peyess.salesapp.database.room.gambeta

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GambetaDao {

    @Insert(onConflict = REPLACE)
    fun add(gambeta: GambetaEntity)

    @Query("SELECT * FROM ${GambetaEntity.tableName} WHERE id = :id")
    fun getGambeta(id: Int): Flow<GambetaEntity?>
}