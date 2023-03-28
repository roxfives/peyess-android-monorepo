package com.peyess.salesapp.dao.products.room.filter_lens_description

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.filter_lens_description.FilterLensDescriptionEntity

@Dao
interface FilterLensDescriptionDao {

    @Insert
    fun add(filter: FilterLensDescriptionEntity)
}