package com.peyess.salesapp.dao.products.room.join_lens_treatment

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface JoinLensTreatmentDao {

    @Insert
    fun add(joinLensTreatmentEntity: JoinLensTreatmentEntity)
}