package com.peyess.salesapp.data.repository.edit_service_order.payment_fee

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.payment_fee.toEditPaymentFeeEntity
import com.peyess.salesapp.data.adapter.edit_service_order.payment_fee.toPaymentFeeDocument
import com.peyess.salesapp.data.dao.edit_service_order.payment_fee.EditPaymentFeeDao
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.DeletePaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.InsertPaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.ReadPaymentFeeError
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error.UpdatePaymentFeeError
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditPaymentFeeRepositoryImpl @Inject constructor(
    private val paymentFeeDao: EditPaymentFeeDao,
): EditPaymentFeeRepository {
    override suspend fun addPaymentFee(
        paymentFee: PaymentFeeDocument,
    ): EditPaymentFeeInsertResponse = Either.catch {
        paymentFeeDao.addPaymentFee(paymentFee.toEditPaymentFeeEntity())
    }.mapLeft {
        InsertPaymentFeeError.Unexpected(
            description = "Error adding payment fee",
            throwable = it,
        )
    }

    override suspend fun paymentFeeForSale(
        saleId: String,
    ): EditPaymentFeeFetchResponse = Either.catch {
        paymentFeeDao.paymentFeeForSale(saleId)?.toPaymentFeeDocument()
    }.mapLeft {
        ReadPaymentFeeError.Unexpected(
            description = "Error reading payment fee",
            throwable = it,
        )
    }.leftIfNull {
        ReadPaymentFeeError.PaymentFeeNotFound(
            description = "Payment fee not found",
        )
    }

    override fun streamPaymentFeeForSale(saleId: String): EditPaymentFeeStreamResponse {
        return paymentFeeDao.streamPaymentFeeForSale(saleId)
            .map {
                if (it == null) {
                    ReadPaymentFeeError.PaymentFeeNotFound(
                        description = "Payment fee not found",
                    ).left()
                } else {
                    it.toPaymentFeeDocument().right()
                }
            }
    }

    override suspend fun updateMethod(
        saleId: String,
        method: PaymentFeeCalcMethod,
    ): EditPaymentFeeUpdateResponse = Either.catch {
        paymentFeeDao.updateMethod(saleId, method)
    }.mapLeft {
        UpdatePaymentFeeError.Unexpected(
            description = "Error while updating fee method for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateValue(
        saleId: String,
        value: Double,
    ): EditPaymentFeeUpdateResponse = Either.catch {
        paymentFeeDao.updateValue(saleId, value)
    }.mapLeft {
        UpdatePaymentFeeError.Unexpected(
            description = "Error while updating fee value for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun deletePaymentFeeForSale(
        saleId: String,
    ): EditPaymentFeeDeleteResponse {
        return Either.catch {
            paymentFeeDao.deletePaymentFeeForSale(saleId)
        }.mapLeft {
            DeletePaymentFeeError.Unexpected(
                description = "Error while deleting payment fee for sale $saleId",
                throwable = it,
            )
        }
    }
}
