package com.peyess.salesapp.data.repository.edit_service_order.payment_fee

import arrow.core.Either
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.InsertPaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.ReadPaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.UpdatePaymentFeeError
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import kotlinx.coroutines.flow.Flow

typealias EditPaymentFeeInsertResponse = Either<InsertPaymentFeeError, Unit>

typealias EditPaymentFetchResponse = Either<ReadPaymentFeeError, PaymentFeeDocument>
typealias EditPaymentStreamResponse = Flow<EditPaymentFetchResponse>

typealias EditPaymentUpdateResponse = Either<UpdatePaymentFeeError, Unit>

interface EditPaymentFeeRepository {
    suspend fun addPaymentFee(paymentFee: PaymentFeeDocument): EditPaymentFeeInsertResponse

    suspend fun paymentFeeForSale(saleId: String): EditPaymentFetchResponse
    fun streamPaymentFeeForSale(saleId: String): EditPaymentStreamResponse

    suspend fun updateMethod(
        saleId: String,
        method: PaymentFeeCalcMethod,
    ): EditPaymentUpdateResponse
    suspend fun updateValue(saleId: String, value: Double): EditPaymentUpdateResponse
}