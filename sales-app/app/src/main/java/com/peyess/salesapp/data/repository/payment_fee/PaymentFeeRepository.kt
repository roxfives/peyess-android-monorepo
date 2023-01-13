package com.peyess.salesapp.data.repository.payment_fee

import arrow.core.Either
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.payment_fee.error.PaymentFeeReadError
import kotlinx.coroutines.flow.Flow

typealias PaymentFeeRepositoryResponse = Either<PaymentFeeReadError, PaymentFeeDocument>

interface PaymentFeeRepository {
    fun watchPaymentFeeForSale(saleId: String): Flow<PaymentFeeDocument?>

    suspend fun getPaymentFeeForSale(saleId: String): PaymentFeeDocument?

    suspend fun updatePaymentFeeForSale(paymentFee: PaymentFeeDocument)

    suspend fun paymentFeeForSale(saleId: String): PaymentFeeRepositoryResponse
}