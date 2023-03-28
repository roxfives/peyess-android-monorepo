package com.peyess.salesapp.dao.products.room.join_lens_tech

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface JoinLensTechDao {

    @Insert
    fun add(joinLensTechEntity: JoinLensTechEntity)
}