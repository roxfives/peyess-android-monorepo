package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensMaterialDao {

    @Insert
    fun add(filter: FilterLensMaterialEntity)

    @Query("SELECT * FROM ${FilterLensMaterialEntity.tableName} WHERE supplier_id = :supplierId")
    fun getAllWithSupplier(supplierId: String): Flow<List<FilterLensMaterialEntity>>
}