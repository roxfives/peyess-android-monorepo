package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity

@Dao
interface LocalLensSupplierDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensSupplierEntity)

    @Query("SELECT * FROM ${LocalLensSupplierEntity.tableName}")
    suspend fun getAll(): List<LocalLensSupplierEntity>

    @Query("SELECT * FROM ${LocalLensSupplierEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensSupplierEntity?
}