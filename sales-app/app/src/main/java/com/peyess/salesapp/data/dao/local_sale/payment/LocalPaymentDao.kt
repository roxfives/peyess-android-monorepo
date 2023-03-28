package com.peyess.salesapp.data.dao.local_sale.payment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalPaymentDao {
    @Insert
    fun add(paymentEntity: LocalPaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(paymentEntity: LocalPaymentEntity)

    @Delete
    fun delete(paymentEntity: LocalPaymentEntity)

    @Query("SELECT * FROM ${LocalPaymentEntity.tableName} WHERE sale_id = :saleId")
    fun watchPaymentsForSale(saleId: String): Flow<List<LocalPaymentEntity>>

    @Query("SELECT * FROM ${LocalPaymentEntity.tableName} WHERE sale_id = :saleId ")
    fun paymentsForSale(saleId: String): List<LocalPaymentEntity>

    @Query("SELECT SUM(value) FROM ${LocalPaymentEntity.tableName} WHERE sale_id = :saleId ")
    fun totalPaymentForSale(saleId: String): Double?

    @Query("SELECT SUM(value) FROM ${LocalPaymentEntity.tableName} WHERE sale_id = :saleId ")
    fun watchTotalPayment(saleId: String): Flow<Double?>

    @Query("SELECT * FROM ${LocalPaymentEntity.tableName} WHERE id = :id ")
    fun watchPayment(id: Long): Flow<LocalPaymentEntity?>

    @Query("SELECT * FROM ${LocalPaymentEntity.tableName} WHERE id = :id ")
    suspend fun payment(id: Long): LocalPaymentEntity?
}