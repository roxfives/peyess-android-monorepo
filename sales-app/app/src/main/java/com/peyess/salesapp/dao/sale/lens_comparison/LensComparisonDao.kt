package com.peyess.salesapp.dao.sale.lens_comparison

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LensComparisonDao {
    @Query(
        "SELECT * FROM ${LensComparisonEntity.tableName} WHERE so_id = :soId"
    )
    fun getBySo(soId: String): Flow<List<LensComparisonEntity>>

    @Insert(onConflict = REPLACE)
    fun add(lensComparisonEntity: LensComparisonEntity)

    @Update(onConflict = REPLACE)
    fun update(activeSale: LensComparisonEntity)
}