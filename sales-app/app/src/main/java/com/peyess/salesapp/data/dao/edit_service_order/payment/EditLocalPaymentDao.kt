package com.peyess.salesapp.data.dao.edit_service_order.payment

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentDBView
import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EditLocalPaymentDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPayment(payment: EditLocalPaymentEntity)

    @Transaction
    @Query("""
        SELECT * FROM ${EditLocalPaymentDBView.viewName}
        WHERE saleId = :saleId
    """)
    suspend fun paymentsForSale(saleId: String): List<EditLocalPaymentDBView>

    @Query("""
        SELECT * FROM ${EditLocalPaymentDBView.viewName}
        WHERE saleId = :saleId
    """)
    fun streamPaymentsForSale(saleId: String): Flow<List<EditLocalPaymentDBView>>

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET client_id = :clientId
        WHERE sale_id = :saleId
    """)
    suspend fun updateClientId(saleId: String, clientId: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET method_id = :methodId
        WHERE sale_id = :saleId
    """)
    suspend fun updateMethodId(saleId: String, methodId: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET method_name = :methodName
        WHERE sale_id = :saleId
    """)
    suspend fun updateMethodName(saleId: String, methodName: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET method_type = :methodType
        WHERE sale_id = :saleId
    """)
    suspend fun updateMethodType(saleId: String, methodType: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET value = :value
        WHERE sale_id = :saleId
    """)
    suspend fun updateValue(saleId: String, value: Double)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET installments = :installments
        WHERE sale_id = :saleId
    """)
    suspend fun updateInstallments(saleId: String, installments: Int)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET document = :document
        WHERE sale_id = :saleId
    """)
    suspend fun updateDocument(saleId: String, document: String)

    @Query("""
        UPDATE ${EditLocalPaymentEntity.tableName}
        SET card_flag_name = :cardFlagName
        WHERE sale_id = :saleId
    """)
    suspend fun updateCardFlagName(saleId: String, cardFlagName: String)

    @Query(
        """
            UPDATE ${EditLocalPaymentEntity.tableName}
            SET card_flag_icon = :cardFlagIcon
            WHERE sale_id = :saleId
        """
    )
    suspend fun updateCardFlagIcon(saleId: String, cardFlagIcon: Uri)
}