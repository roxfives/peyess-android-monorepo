package com.peyess.salesapp.data.repository.edit_service_order.payment_fee

import arrow.core.Either
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.InsertPaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.ReadPaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.UpdatePaymentFeeError
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import kotlinx.coroutines.flow.Flow

typealias EditPaymentFeeInsertResponse = Either<InsertPaymentFeeError, Unit>

typealias EditPaymentFeeFetchResponse = Either<ReadPaymentFeeError, PaymentFeeDocument>
typealias EditPaymentFeeStreamResponse = Flow<EditPaymentFeeFetchResponse>

typealias EditPaymentFeeUpdateResponse = Either<UpdatePaymentFeeError, Unit>

interface EditPaymentFeeRepository {
    suspend fun addPaymentFee(paymentFee: PaymentFeeDocument): EditPaymentFeeInsertResponse

    suspend fun paymentFeeForSale(saleId: String): EditPaymentFeeFetchResponse
    fun streamPaymentFeeForSale(saleId: String): EditPaymentFeeStreamResponse

    suspend fun updateMethod(
        saleId: String,
        method: PaymentFeeCalcMethod,
    ): EditPaymentFeeUpdateResponse
    suspend fun updateValue(saleId: String, value: Double): EditPaymentFeeUpdateResponse
}