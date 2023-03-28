package com.peyess.salesapp.dao.products.room.filter_lens_category

import androidx.room.Dao
import androidx.room.Insert
import com.peyess.salesapp.dao.products.room.filter_lens_category.FilterLensCategoryEntity

@Dao
interface FilterLensCategoryDao {

    @Insert
    fun add(filter: FilterLensCategoryEntity)
}