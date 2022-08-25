package com.peyess.salesapp.dao.products.room.join_lens_treatment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface JoinLensTreatmentDao {

    @Insert(onConflict = REPLACE)
    fun add(joinLensTreatmentEntity: JoinLensTreatmentEntity)
}