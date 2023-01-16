package com.peyess.salesapp.data.dao.local_sale.payment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalePaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(paymentEntity: SalePaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(paymentEntity: SalePaymentEntity)

    @Delete
    fun delete(paymentEntity: SalePaymentEntity)

    @Query("SELECT * FROM ${SalePaymentEntity.tableName} WHERE sale_id = :saleId")
    fun watchPaymentsForSale(saleId: String): Flow<List<SalePaymentEntity>>

    @Query("SELECT * FROM ${SalePaymentEntity.tableName} WHERE sale_id = :saleId ")
    fun paymentsForSale(saleId: String): List<SalePaymentEntity>

    @Query("SELECT SUM(value) FROM ${SalePaymentEntity.tableName} WHERE sale_id = :saleId ")
    fun totalPaymentForSale(saleId: String): Double

    @Query("SELECT SUM(value) FROM ${SalePaymentEntity.tableName} WHERE sale_id = :saleId ")
    fun watchTotalPayment(saleId: String): Flow<Double>

    @Query("SELECT * FROM ${SalePaymentEntity.tableName} WHERE id = :id ")
    fun watchPayment(id: Long): Flow<SalePaymentEntity?>

    @Query("SELECT * FROM ${SalePaymentEntity.tableName} WHERE id = :id ")
    suspend fun payment(id: Long): SalePaymentEntity?
}