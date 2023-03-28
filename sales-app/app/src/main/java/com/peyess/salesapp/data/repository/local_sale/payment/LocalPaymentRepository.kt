package com.peyess.salesapp.data.repository.local_sale.payment

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.local_sale.payment.error.LocalPaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.LocalPaymentWriteError
import kotlinx.coroutines.flow.Flow

typealias LocalPaymentResponse = Either<LocalPaymentReadError, List<LocalPaymentDocument>>
typealias SinglePaymentResponse = Either<LocalPaymentReadError, LocalPaymentDocument>
typealias LocalPaymentTotalResponse = Either<LocalPaymentReadError, Double>
typealias LocalPaymentWriteResult = Either<LocalPaymentWriteError, Long>
typealias LocalPaymentUpdateResult = Either<LocalPaymentWriteError, Unit>
typealias LocalPaymentDeleteResult = Either<LocalPaymentWriteError, Unit>

interface LocalPaymentRepository {
    suspend fun paymentForSale(saleId: String): LocalPaymentResponse

    fun watchPaymentsForSale(saleId: String): Flow<LocalPaymentResponse>

    fun watchPayment(paymentId: Long): Flow<SinglePaymentResponse>

    fun watchTotalPayment(saleId: String): Flow<LocalPaymentTotalResponse>

    suspend fun payment(paymentId: Long): SinglePaymentResponse

    suspend fun totalPaymentForSale(saleId: String): LocalPaymentTotalResponse

    suspend fun addPaymentToSale(paymentDocument: LocalPaymentDocument): LocalPaymentWriteResult

    suspend fun updatePayment(paymentDocument: LocalPaymentDocument): LocalPaymentUpdateResult

    suspend fun deletePayment(paymentDocument: LocalPaymentDocument): LocalPaymentDeleteResult
}
