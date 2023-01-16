package com.peyess.salesapp.data.dao.payment_fee

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentFeeDao {
    @Insert(onConflict = REPLACE)
    fun updatePaymentFee(paymentFee: PaymentFeeEntity)

    @Query("SELECT * FROM ${PaymentFeeEntity.tableName} WHERE sale_id = :saleId")
    suspend fun getPaymentFee(saleId: String): PaymentFeeEntity?

    @Query("SELECT * FROM ${PaymentFeeEntity.tableName} WHERE sale_id = :saleId")
    fun watchPaymentFee(saleId: String): Flow<PaymentFeeEntity?>
}