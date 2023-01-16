package com.peyess.salesapp.data.repository.local_sale.payment

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentWriteError
import kotlinx.coroutines.flow.Flow

typealias SalePaymentFlowResponse = Flow<Either<SalePaymentReadError, SalePaymentDocument>>
typealias SalePaymentResponse = Either<SalePaymentReadError, List<SalePaymentDocument>>
typealias SinglePaymentResponse = Either<SalePaymentReadError, SalePaymentDocument>
typealias SalePaymentTotalResponse = Either<SalePaymentReadError, Double>
typealias SalePaymentWriteResult = Either<SalePaymentWriteError, Long>
typealias SalePaymentUpdateResult = Either<SalePaymentWriteError, Unit>
typealias SalePaymentDeleteResult = Either<SalePaymentWriteError, Unit>

interface SalePaymentRepository {
    suspend fun paymentForSale(saleId: String): SalePaymentResponse

    fun watchPayment(paymentId: Long): SalePaymentFlowResponse

    suspend fun payment(paymentId: Long): SinglePaymentResponse

    suspend fun totalPaymentForSale(saleId: String): SalePaymentTotalResponse

    suspend fun addPaymentToSale(paymentDocument: SalePaymentDocument): SalePaymentWriteResult

    suspend fun updatePayment(paymentDocument: SalePaymentDocument): SalePaymentUpdateResult

    suspend fun deletePayment(paymentDocument: SalePaymentDocument): SalePaymentDeleteResult
}
