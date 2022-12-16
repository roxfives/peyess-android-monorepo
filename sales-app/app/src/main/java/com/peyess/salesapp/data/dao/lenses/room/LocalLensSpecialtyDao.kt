package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity

@Dao
interface LocalLensSpecialtyDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensSpecialtyEntity)

    @Query("SELECT * FROM ${LocalLensSpecialtyEntity.tableName}")
    suspend fun getAll(): List<LocalLensSpecialtyEntity>

    @Query("SELECT * FROM ${LocalLensSpecialtyEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensSpecialtyEntity?
}