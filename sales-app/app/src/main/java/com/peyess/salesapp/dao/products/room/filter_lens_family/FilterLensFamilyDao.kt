package com.peyess.salesapp.dao.products.room.filter_lens_family

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensFamilyDao {

    @Insert
    fun add(filter: FilterLensFamilyEntity)

    @Query("SELECT * FROM ${FilterLensFamilyEntity.tableName} WHERE supplier_id = :supplierId")
    fun getFamiliesWithSupplier(supplierId: String): Flow<List<FilterLensFamilyEntity>>
}