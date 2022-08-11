package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface FilterLensSupplierDao {

    @Insert
    fun add(filter: FilterLensSupplierEntity)
}