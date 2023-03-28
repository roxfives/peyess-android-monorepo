package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensTypeDao {

    @Insert
    fun add(filter: FilterLensTypeEntity)

    @Query("SELECT * FROM ${FilterLensTypeEntity.tableName} ORDER BY name")
    fun getAll(): Flow<List<FilterLensTypeEntity>>
}