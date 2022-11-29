package com.peyess.salesapp.data.dao.discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.discount.OverallDiscountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OverallDiscountDao {
    @Insert(onConflict = REPLACE)
    fun updateDiscount(discount: OverallDiscountEntity)

    @Query("SELECT * FROM ${OverallDiscountEntity.tableName} WHERE sale_id = :saleId")
    suspend fun getDiscount(saleId: String): OverallDiscountEntity?

    @Query("SELECT * FROM ${OverallDiscountEntity.tableName} WHERE sale_id = :saleId")
    fun watchDiscount(saleId: String): Flow<OverallDiscountEntity?>
}