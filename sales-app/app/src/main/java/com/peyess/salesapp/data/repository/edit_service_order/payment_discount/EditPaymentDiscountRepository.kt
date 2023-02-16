package com.peyess.salesapp.data.repository.edit_service_order.payment_discount

import arrow.core.Either
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.InsertPaymentDiscountError
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.ReadPaymentDiscountError
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.UpdatePaymentDiscountError
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import kotlinx.coroutines.flow.Flow

typealias EditPaymentDiscountInsertResponse = Either<InsertPaymentDiscountError, Unit>

typealias EditPaymentDiscountFetchResponse = Either<ReadPaymentDiscountError, OverallDiscountDocument>
typealias EditPaymentDiscountStreamResponse = Flow<EditPaymentDiscountFetchResponse>

typealias EditPaymentDiscountUpdateResponse = Either<UpdatePaymentDiscountError, Unit>

interface EditPaymentDiscountRepository {
    suspend fun addPaymentDiscount(
        paymentDiscount: OverallDiscountDocument,
    ): EditPaymentDiscountInsertResponse

    suspend fun discountForSale(saleId: String): EditPaymentDiscountFetchResponse
    fun streamDiscountForSale(saleId: String): EditPaymentDiscountStreamResponse

    suspend fun updateMethod(saleId: String, method: DiscountCalcMethod): EditPaymentDiscountUpdateResponse
    suspend fun updateValue(saleId: String, value: Double): EditPaymentDiscountUpdateResponse
}