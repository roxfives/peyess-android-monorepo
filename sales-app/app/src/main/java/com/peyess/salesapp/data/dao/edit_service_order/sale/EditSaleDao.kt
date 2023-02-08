package com.peyess.salesapp.data.dao.edit_service_order.sale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EditSaleDao {
    @Insert(onConflict = REPLACE)
    suspend fun addSale(sale: EditSaleEntity)

    @Query("SELECT * FROM ${EditSaleEntity.tableName} WHERE id = :id")
    fun streamSaleById(id: String): Flow<EditSaleEntity?>

    @Query("SELECT * FROM ${EditSaleEntity.tableName} WHERE id = :id")
    suspend fun findSaleById(id: String): EditSaleEntity?

    @Query("""
        UPDATE ${EditSaleEntity.tableName}
        SET is_uploading = :isUploading
        WHERE id = :id
    """)
    suspend fun updateIsUploading(id: String, isUploading: Boolean)
}
