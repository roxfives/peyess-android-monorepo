package com.peyess.salesapp.dao.products.room.join_lens_material

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface JoinLensMaterialDao {

    @Insert(onConflict = REPLACE)
    fun add(joinLensMaterialEntity: JoinLensMaterialEntity)
}