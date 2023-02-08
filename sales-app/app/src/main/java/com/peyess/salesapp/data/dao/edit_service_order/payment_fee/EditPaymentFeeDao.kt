package com.peyess.salesapp.data.dao.edit_service_order.payment_fee

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.payment_fee.EditPaymentFeeEntity
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import kotlinx.coroutines.flow.Flow

@Dao
interface EditPaymentFeeDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPaymentFee(paymentFee: EditPaymentFeeEntity)

    @Query("""
        SELECT * FROM ${EditPaymentFeeEntity.tableName}
        WHERE sale_id = :saleId
    """)
    suspend fun paymentFeeForSale(saleId: String): EditPaymentFeeEntity?

    @Query("""
        SELECT * FROM ${EditPaymentFeeEntity.tableName}
        WHERE sale_id = :saleId
    """)
    fun streamPaymentFeeForSale(saleId: String): Flow<EditPaymentFeeEntity?>

    @Query("""
        UPDATE ${EditPaymentFeeEntity.tableName}
        SET method = :method
        WHERE sale_id = :saleId
    """)
    suspend fun updateMethod(saleId: String, method: PaymentFeeCalcMethod)

    @Query("""
        UPDATE ${EditPaymentFeeEntity.tableName}
        SET value = :value
        WHERE sale_id = :saleId
    """)
    suspend fun updateValue(saleId: String, value: Double)
}