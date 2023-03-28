package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensSpecialtyDao {

    @Insert
    fun add(filter: FilterLensSpecialtyEntity)

    @Query("SELECT * FROM ${FilterLensSpecialtyEntity.tableName} AS s ORDER BY s.name ASC")
    fun getAll(): Flow<List<FilterLensSpecialtyEntity>>
}