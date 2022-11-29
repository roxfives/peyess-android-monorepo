package com.peyess.salesapp.data.repository.payment_fee

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import kotlinx.coroutines.flow.Flow

interface PaymentFeeRepository {
    fun watchPaymentFeeForSale(saleId: String): Flow<PaymentFeeDocument?>

    suspend fun getPaymentFeeForSale(saleId: String): PaymentFeeDocument?

    suspend fun updatePaymentFeeForSale(paymentFee: PaymentFeeDocument)
}