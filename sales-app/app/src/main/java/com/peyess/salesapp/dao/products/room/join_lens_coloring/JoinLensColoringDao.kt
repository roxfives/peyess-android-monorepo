package com.peyess.salesapp.dao.products.room.join_lens_coloring

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface JoinLensColoringDao {
    @Insert
    fun add(joinLensColoringEntity: JoinLensColoringEntity)
}