package com.peyess.salesapp.data.dao.edit_service_order.payment

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EditLocalPaymentDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPayment(payment: EditLocalPaymentEntity)

    @Query("""
        DELETE FROM ${EditLocalPaymentEntity.tableName}
        WHERE id = :paymentId
    """)
    suspend fun deletePayment(paymentId: Long)

    @Query("""
        SELECT SUM(value) FROM ${EditLocalPaymentEntity.tableName}
        WHERE sale_id = :saleId
    """)
    suspend fun totalPaid(saleId: String): Double?

    @Query("""
        SELECT SUM(value) FROM ${EditLocalPaymentEntity.tableName}
        WHERE sale_id = :saleId
    """)
    fun streamTotalPaid(saleId: String): Flow<Double?>

    @Transaction
    @Query("""
        SELECT * FROM ${EditLocalPaymentEntity.tableName}
        WHERE sale_id = :saleId
    """)
    suspend fun paymentsForSale(saleId: String): List<EditLocalPaymentEntity>

    @Query("""
        SELECT * FROM ${EditLocalPaymentEntity.tableName}
        WHERE sale_id = :saleId
    """)
    fun streamPaymentsForSale(saleId: String): Flow<List<EditLocalPaymentEntity>>

    @Query("""
        SELECT * FROM ${EditLocalPaymentEntity.tableName}
        WHERE id = :paymentId
    """)
    suspend fun paymentById(paymentId: Long): EditLocalPaymentEntity?

    @Query("""
        SELECT * FROM ${EditLocalPaymentEntity.tableName}
        WHERE id = :paymentId
    """)
    fun streamPaymentById(paymentId: Long): Flow<EditLocalPaymentEntity?>

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET client_id = :clientId
        WHERE id = :paymentId
    """)
    suspend fun updateClientId(paymentId: Long, clientId: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET method_id = :methodId
        WHERE id = :paymentId
    """)
    suspend fun updateMethodId(paymentId: Long, methodId: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET method_name = :methodName
        WHERE id = :paymentId
    """)
    suspend fun updateMethodName(paymentId: Long, methodName: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET method_type = :methodType
        WHERE id = :paymentId
    """)
    suspend fun updateMethodType(paymentId: Long, methodType: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET value = :value
        WHERE id = :paymentId
    """)
    suspend fun updateValue(paymentId: Long, value: Double)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET installments = :installments
        WHERE id = :paymentId
    """)
    suspend fun updateInstallments(paymentId: Long, installments: Int)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET document = :document
        WHERE id = :paymentId
    """)
    suspend fun updateDocument(paymentId: Long, document: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET card_flag_name = :cardFlagName
        WHERE id = :paymentId
    """)
    suspend fun updateCardFlagName(paymentId: Long, cardFlagName: String)

    @Query(
        """
            UPDATE ${EditLocalPaymentEntity.tableName}
            SET card_flag_icon = :cardFlagIcon
            WHERE id = :paymentId
        """)
    suspend fun updateCardFlagIcon(paymentId: Long, cardFlagIcon: Uri)
}