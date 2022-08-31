package com.peyess.salesapp.dao.sale.payment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SalePaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(paymentEntity: SalePaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(paymentEntity: SalePaymentEntity)

    @Query("SELECT * FROM ${SalePaymentEntity.tableName} WHERE so_id = :soId ")
    fun getBySO(soId: String): Flow<List<SalePaymentEntity>>

    @Query("SELECT * FROM ${SalePaymentEntity.tableName} WHERE id = :id ")
    fun getById(id: Long): Flow<SalePaymentEntity?>
}