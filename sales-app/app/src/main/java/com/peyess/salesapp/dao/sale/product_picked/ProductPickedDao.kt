package com.peyess.salesapp.dao.sale.product_picked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductPickedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(productPicked: ProductPickedEntity)

    @Query("SELECT * FROM ${ProductPickedEntity.tableName} WHERE so_id = :soId ")
    fun getById(soId: String): Flow<ProductPickedEntity?>
}