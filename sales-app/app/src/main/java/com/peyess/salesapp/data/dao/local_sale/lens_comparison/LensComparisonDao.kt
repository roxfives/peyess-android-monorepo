package com.peyess.salesapp.data.dao.local_sale.lens_comparison

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonEntity

@Dao
interface LensComparisonDao {
    @Query("SELECT * FROM ${LensComparisonEntity.tableName} WHERE so_id = :soId")
    suspend fun getBySo(soId: String): List<LensComparisonEntity>

    @Query("DELETE FROM ${LensComparisonEntity.tableName} WHERE id = :comparisonId")
    suspend fun deleteById(comparisonId: Int)

    @Query("DELETE FROM ${LensComparisonEntity.tableName} WHERE so_id = :soId")
    suspend fun deleteAllFromSO(soId: String)

    @Insert(onConflict = REPLACE)
    suspend fun add(lensComparisonEntity: LensComparisonEntity)

    @Update(onConflict = REPLACE)
    suspend fun update(lensComparisonEntity: LensComparisonEntity)
}
