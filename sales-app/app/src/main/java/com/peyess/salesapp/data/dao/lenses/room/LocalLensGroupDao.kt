package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity

@Dao
interface LocalLensGroupDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensGroupEntity)

    @Query("SELECT * FROM ${LocalLensGroupEntity.tableName}")
    suspend fun getAll(): List<LocalLensGroupEntity>

    @Query("SELECT * FROM ${LocalLensGroupEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensGroupEntity?
}