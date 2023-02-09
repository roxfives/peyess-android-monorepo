package com.peyess.salesapp.data.repository.edit_service_order.payment_discount

import arrow.core.Either
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.edit_service_order.payment_discount.EditOverallDiscountEntity
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditSalePaymentStreamResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.InsertPaymentDiscountError
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.ReadPaymentDiscountError
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.UpdatePaymentDiscountError
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import kotlinx.coroutines.flow.Flow

typealias EditPaymentDiscountInsertResponse = Either<InsertPaymentDiscountError, Unit>

typealias EditPaymentFetchResponse = Either<ReadPaymentDiscountError, OverallDiscountDocument>
typealias EditPaymentStreamResponse = Flow<EditPaymentFetchResponse>

typealias EditPaymentUpdateResponse = Either<UpdatePaymentDiscountError, Unit>

interface EditPaymentDiscountRepository {
    suspend fun addPaymentDiscount(
        paymentDiscount: OverallDiscountDocument,
    ): EditPaymentDiscountInsertResponse

    suspend fun discountForSale(saleId: String): EditPaymentFetchResponse
    fun streamDiscountForSale(saleId: String): EditPaymentStreamResponse

    suspend fun updateMethod(saleId: String, method: DiscountCalcMethod): EditPaymentUpdateResponse
    suspend fun updateValue(saleId: String, value: Double): EditPaymentUpdateResponse
}