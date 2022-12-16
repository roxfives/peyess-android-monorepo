package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity

@Dao
interface LocalLensFamilyDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensFamilyEntity)

    @Query("SELECT * FROM ${LocalLensFamilyEntity.tableName}")
    suspend fun getAll(): List<LocalLensFamilyEntity>

    @Query("SELECT * FROM ${LocalLensFamilyEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensFamilyEntity?
}