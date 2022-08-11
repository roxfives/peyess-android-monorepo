package com.peyess.salesapp.dao.products.room.local_lens

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface LocalLensDao {
    @Insert
    fun add(lensEntity: LocalLensEntity)
}