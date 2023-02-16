package com.peyess.salesapp.data.repository.edit_service_order.payment_discount

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.payment_discount.toEditOverallDiscountEntity
import com.peyess.salesapp.data.adapter.edit_service_order.payment_discount.toOverallDiscountDocument
import com.peyess.salesapp.data.dao.edit_service_order.payment_discount.EditOverallDiscountDao
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.InsertPaymentDiscountError
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.ReadPaymentDiscountError
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error.UpdatePaymentDiscountError
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditPaymentDiscountRepositoryImpl @Inject constructor(
    private val paymentDiscountDao: EditOverallDiscountDao,
): EditPaymentDiscountRepository {
    override suspend fun addPaymentDiscount(
        paymentDiscount: OverallDiscountDocument,
    ): EditPaymentDiscountInsertResponse = Either.catch {
        paymentDiscountDao.addPaymentDiscount(paymentDiscount.toEditOverallDiscountEntity())
    }.mapLeft {
        InsertPaymentDiscountError.Unexpected(
            description = "Error adding payment discount",
            throwable = it,
        )
    }

    override suspend fun discountForSale(
        saleId: String,
    ): EditPaymentDiscountFetchResponse = Either.catch {
        paymentDiscountDao.discountForSale(saleId)?.toOverallDiscountDocument()
    }.mapLeft {
        ReadPaymentDiscountError.Unexpected(
            description = "Error reading payment discount",
            throwable = it,
        )
    }.leftIfNull {
        ReadPaymentDiscountError.PaymentDiscountNotFound(
            description = "Payment discount not found",
        )
    }

    override fun streamDiscountForSale(saleId: String): EditPaymentDiscountStreamResponse {
        return paymentDiscountDao.streamDiscountForSale(saleId)
            .map {
                if (it == null) {
                    ReadPaymentDiscountError.PaymentDiscountNotFound(
                        description = "Payment discount not found",
                    ).left()
                } else {
                    it.toOverallDiscountDocument().right()
                }
            }
    }

    override suspend fun updateMethod(
        saleId: String,
        method: DiscountCalcMethod,
    ): EditPaymentDiscountUpdateResponse = Either.catch {
        paymentDiscountDao.updateMethod(saleId, method)
    }.mapLeft {
        UpdatePaymentDiscountError.Unexpected(
            description = "Error while updating discount method for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateValue(
        saleId: String,
        value: Double,
    ): EditPaymentDiscountUpdateResponse = Either.catch {
        paymentDiscountDao.updateValue(saleId, value)
    }.mapLeft {
        UpdatePaymentDiscountError.Unexpected(
            description = "Error while updating discount value for sale $saleId",
            throwable = it,
        )
    }
}