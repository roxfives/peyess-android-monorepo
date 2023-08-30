package com.peyess.salesapp.data.repository.edit_service_order.payment

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.DeleteLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.InsertLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.ReadLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.UpdateLocalPaymentError
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.ZonedDateTime

typealias EditLocalPaymentInsertResponse = Either<InsertLocalPaymentError, Long>
typealias EditLocalPaymentDeleteResponse = Either<DeleteLocalPaymentError, Unit>

typealias EditLocalPaymentFetchResponse = Either<ReadLocalPaymentError, List<LocalPaymentDocument>>
typealias EditLocalPaymentStreamResponse = Flow<EditLocalPaymentFetchResponse>

typealias EditLocalPaymentFetchSingleResponse = Either<ReadLocalPaymentError, LocalPaymentDocument>
typealias EditLocalPaymentStreamSingleResponse = Flow<EditLocalPaymentFetchSingleResponse>

typealias EditLocalPaymentFetchTotalResponse = Either<ReadLocalPaymentError, BigDecimal>
typealias EditLocalPaymentStreamTotalResponse = Flow<EditLocalPaymentFetchTotalResponse>

typealias EditLocalPaymentUpdateResponse = Either<UpdateLocalPaymentError, Unit>

interface EditLocalPaymentRepository {
    suspend fun addPayment(payment: LocalPaymentDocument): EditLocalPaymentInsertResponse
    suspend fun deletePayment(paymentId: Long): EditLocalPaymentDeleteResponse

    suspend fun totalPaid(saleId: String): EditLocalPaymentFetchTotalResponse
    fun streamTotalPaid(saleId: String): EditLocalPaymentStreamTotalResponse

    suspend fun paymentsForSale(saleId: String): EditLocalPaymentFetchResponse
    fun streamPaymentsForSale(saleId: String): EditLocalPaymentStreamResponse

    suspend fun paymentById(paymentId: Long, clientId: String,): EditLocalPaymentFetchSingleResponse
    fun streamPaymentById(paymentId: Long, clientId: String,): EditLocalPaymentStreamSingleResponse

    suspend fun updateClientId(paymentId: Long, clientId: String): EditLocalPaymentUpdateResponse
    suspend fun updateMethodId(paymentId: Long, methodId: String): EditLocalPaymentUpdateResponse
    suspend fun updateMethodName(paymentId: Long, methodName: String): EditLocalPaymentUpdateResponse
    suspend fun updateMethodType(paymentId: Long, methodType: String): EditLocalPaymentUpdateResponse
    suspend fun updateValue(paymentId: Long, value: BigDecimal): EditLocalPaymentUpdateResponse
    suspend fun updateInstallments(paymentId: Long, installments: Int): EditLocalPaymentUpdateResponse
    suspend fun updateDocument(paymentId: Long, document: String): EditLocalPaymentUpdateResponse
    suspend fun updateCardFlagName(paymentId: Long, cardFlagName: String): EditLocalPaymentUpdateResponse
    suspend fun updateCardFlagIcon(paymentId: Long, cardFlagIcon: Uri): EditLocalPaymentUpdateResponse

    suspend fun updateLegalId(paymentId: Long, legalId: String): EditLocalPaymentUpdateResponse
    suspend fun updateHasLegalId(paymentId: Long, hasLegalId: Boolean): EditLocalPaymentUpdateResponse

    suspend fun updateDueDateData(
        paymentId: Long,
        dueDateMode: PaymentDueDateMode,
        dueDatePeriod: Int,
        dueDate: ZonedDateTime,
    ): EditLocalPaymentUpdateResponse

    suspend fun deletePaymentsForSale(saleId: String): EditLocalPaymentDeleteResponse
}
