package com.peyess.salesapp.dao.products.room.local_product_exp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalProductExpDao {
    @Insert(onConflict = REPLACE)
    fun add(exp: LocalProductExpEntity)

    @Query("SELECT * FROM ${LocalProductExpEntity.tableName} WHERE product_id == :productId")
    fun getByProductId(productId: String): Flow<List<LocalProductExpEntity>>
}