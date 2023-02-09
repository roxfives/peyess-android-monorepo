package com.peyess.salesapp.data.repository.edit_service_order.payment

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.InsertLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.ReadLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.UpdateLocalPaymentError
import kotlinx.coroutines.flow.Flow

typealias EditLocalPaymentInsertResponse = Either<InsertLocalPaymentError, Unit>

typealias EditLocalPaymentFetchResponse = Either<ReadLocalPaymentError, List<LocalPaymentDocument>>
typealias EditLocalPaymentStreamResponse = Flow<EditLocalPaymentFetchResponse>

typealias EditLocalPaymentUpdateResponse = Either<UpdateLocalPaymentError, Unit>

interface EditLocalPaymentRepository {
    suspend fun addPayment(payment: LocalPaymentDocument): EditLocalPaymentInsertResponse

    suspend fun paymentsForSale(saleId: String): EditLocalPaymentFetchResponse
    fun streamPaymentsForSale(saleId: String): EditLocalPaymentStreamResponse

    suspend fun updateClientId(saleId: String, clientId: String): EditLocalPaymentUpdateResponse
    suspend fun updateMethodId(saleId: String, methodId: String): EditLocalPaymentUpdateResponse
    suspend fun updateMethodName(saleId: String, methodName: String): EditLocalPaymentUpdateResponse
    suspend fun updateMethodType(saleId: String, methodType: String): EditLocalPaymentUpdateResponse
    suspend fun updateValue(saleId: String, value: Double): EditLocalPaymentUpdateResponse
    suspend fun updateInstallments(saleId: String, installments: Int): EditLocalPaymentUpdateResponse
    suspend fun updateDocument(saleId: String, document: String): EditLocalPaymentUpdateResponse
    suspend fun updateCardFlagName(saleId: String, cardFlagName: String): EditLocalPaymentUpdateResponse
    suspend fun updateCardFlagIcon(saleId: String, cardFlagIcon: Uri): EditLocalPaymentUpdateResponse
}
