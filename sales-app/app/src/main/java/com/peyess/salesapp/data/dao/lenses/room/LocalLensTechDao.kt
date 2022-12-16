package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity

@Dao
interface LocalLensTechDao {
    @Insert(onConflict = REPLACE)
    fun add(entity: LocalLensTechEntity)

    @Query("SELECT * FROM ${LocalLensTechEntity.tableName}")
    suspend fun getAll(): List<LocalLensTechEntity>

    @Query("SELECT * FROM ${LocalLensTechEntity.tableName} WHERE id = :id")
    suspend fun getById(id: String): LocalLensTechEntity?
}