package com.peyess.salesapp.data.dao.edit_service_order.product_picked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.product_picked.EditProductPickedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EditProductPickedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductPicked(productPicked: EditProductPickedEntity)

    @Query("""
        SELECT * FROM ${EditProductPickedEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun productPickedForServiceOrder(serviceOrderId: String): EditProductPickedEntity?

    @Query("""
        SELECT * FROM ${EditProductPickedEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    fun streamProductPickedForServiceOrder(serviceOrderId: String): Flow<EditProductPickedEntity?>

    @Query("""
        UPDATE ${EditProductPickedEntity.tableName}
        SET lens_id = :lensId
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateLensId(serviceOrderId: String, lensId: String)

    @Query("""
        UPDATE ${EditProductPickedEntity.tableName}
        SET treatment_id = :coloringId
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateColoringId(serviceOrderId: String, coloringId: String)

    @Query("""
        UPDATE ${EditProductPickedEntity.tableName}
        SET treatment_id = :treatmentId
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateTreatmentId(serviceOrderId: String, treatmentId: String)

    @Query("""
        DELETE FROM ${EditProductPickedEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun deleteProductPickedForServiceOrder(serviceOrderId: String)
}
