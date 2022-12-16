package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity

@Dao
interface LocalLensDescriptionDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensDescriptionEntity)

    @Query("SELECT * FROM ${LocalLensDescriptionEntity.tableName}")
    suspend fun getAll(): List<LocalLensDescriptionEntity>

    @Query("SELECT * FROM ${LocalLensDescriptionEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensDescriptionEntity?
}