package com.peyess.salesapp.dao.products.room.local_lens_disp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalLensDispDao {
    @Insert(onConflict = REPLACE)
    fun add(lensDispEntity: LocalLensDispEntity)

    @Query("SELECT * FROM ${LocalLensDispEntity.tableName} WHERE lens_id = :lensId")
    fun getLensDisp(lensId: String): Flow<List<LocalLensDispEntity>>
}