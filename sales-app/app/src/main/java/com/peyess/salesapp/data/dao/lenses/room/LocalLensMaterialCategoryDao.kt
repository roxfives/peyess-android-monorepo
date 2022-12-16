package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialCategoryEntity

@Dao
interface LocalLensMaterialCategoryDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensMaterialCategoryEntity)

    @Query("SELECT * FROM ${LocalLensMaterialCategoryEntity.tableName}")
    suspend fun getAll(): List<LocalLensMaterialCategoryEntity>

    @Query("SELECT * FROM ${LocalLensMaterialCategoryEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensMaterialCategoryEntity?
}