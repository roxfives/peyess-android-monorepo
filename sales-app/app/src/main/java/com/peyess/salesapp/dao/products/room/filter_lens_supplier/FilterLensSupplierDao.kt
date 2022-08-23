package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensSupplierDao {

    @Insert(onConflict = REPLACE)
    fun add(filter: FilterLensSupplierEntity)

    @Query("SELECT * FROM ${FilterLensSupplierEntity.tableName} AS s ORDER BY s.name ASC")
    fun getAll(): Flow<List<FilterLensSupplierEntity>>
}