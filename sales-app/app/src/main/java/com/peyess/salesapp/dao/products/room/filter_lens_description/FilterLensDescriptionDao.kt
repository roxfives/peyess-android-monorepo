package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionEntity

@Dao
interface FilterLensDescriptionDao {

    @Insert
    fun add(filter: FilterLensDescriptionEntity)
}