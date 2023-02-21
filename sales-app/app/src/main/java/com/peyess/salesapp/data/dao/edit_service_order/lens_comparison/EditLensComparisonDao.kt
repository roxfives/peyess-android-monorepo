package com.peyess.salesapp.data.dao.edit_service_order.lens_comparison

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.lens_comparison.EditLensComparisonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EditLensComparisonDao {
    @Insert(onConflict = REPLACE)
    suspend fun addLensComparison(lensComparison: EditLensComparisonEntity)

    @Query("""
        SELECT * FROM ${EditLensComparisonEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    fun streamLensComparisonsForServiceOrder(
        serviceOrderId: String,
    ): Flow<EditLensComparisonEntity>

    @Query("""
        DELETE FROM ${EditLensComparisonEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun deleteComparisonsForServiceOrder(serviceOrderId: String)
}
