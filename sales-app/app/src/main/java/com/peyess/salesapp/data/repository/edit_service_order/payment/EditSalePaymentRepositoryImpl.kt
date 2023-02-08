package com.peyess.salesapp.data.repository.edit_service_order.payment

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.peyess.salesapp.dao.sale.active_sale.LocalSaleDocument
import com.peyess.salesapp.data.adapter.edit_service_order.payment.toEditSalePaymentEntity
import com.peyess.salesapp.data.adapter.edit_service_order.payment.toSalePaymentDocument
import com.peyess.salesapp.data.adapter.edit_service_order.sale.toEditSaleEntity
import com.peyess.salesapp.data.dao.edit_service_order.payment.EditSalePaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.InsertSalePaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.ReadSalePaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.UpdateSalePaymentError
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentWriteError
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import timber.log.Timber
import javax.inject.Inject

private const val attemptThreshold = 10L

class EditSalePaymentRepositoryImpl @Inject constructor(
    private val editSalePaymentDao: EditSalePaymentDao,
): EditSalePaymentRepository {
    override suspend fun addPayment(
        payment: SalePaymentDocument,
    ): EditSalePaymentInsertResponse = Either.catch {
        editSalePaymentDao.addPayment(payment.toEditSalePaymentEntity())
    }.mapLeft {
        InsertSalePaymentError.Unexpected(
            description = "Error while inserting payment $payment",
            throwable = it,
        )
    }

    override suspend fun paymentsForSale(
        saleId: String,
    ): EditSaleFetchResponse = Either.catch {
        editSalePaymentDao.paymentsForSale(saleId).map {
            it.toSalePaymentDocument()
        }
    }.mapLeft {
        ReadSalePaymentError.Unexpected(
            description = "Error while fetching payments for sale $saleId",
            throwable = it,
        )
    }

    override fun streamPaymentsForSale(
        saleId: String,
    ): EditSalePaymentStreamResponse {
        return editSalePaymentDao.streamPaymentsForSale(saleId)
            .map {
                it.map { payment ->
                    payment.toSalePaymentDocument()
                }.right()
            }.retryWhen { cause, attempt ->
                Timber.e(cause, "Error while fetching payments for sale " +
                        "$saleId (attmept $attempt")

                attempt < attemptThreshold
            }

    }

    override suspend fun updateClientId(
        saleId: String,
        clientId: String,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateClientId(saleId, clientId)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment clientId for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateMethodId(
        saleId: String,
        methodId: String,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateMethodId(saleId, methodId)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment methodId for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateMethodName(
        saleId: String,
        methodName: String,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateMethodName(saleId, methodName)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment methodName for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateMethodType(
        saleId: String,
        methodType: String,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateMethodType(saleId, methodType)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment methodType for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateValue(
        saleId: String,
        value: Double,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateValue(saleId, value)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment value for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateInstallments(
        saleId: String,
        installments: Int,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateInstallments(saleId, installments)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment installments for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateDocument(
        saleId: String,
        document: String,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateDocument(saleId, document)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment document for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateCardFlagName(
        saleId: String,
        cardFlagName: String,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateCardFlagName(saleId, cardFlagName)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment cardFlagName for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateCardFlagIcon(
        saleId: String,
        cardFlagIcon: Uri,
    ): EditSalePaymentUpdateResponse = Either.catch {
        editSalePaymentDao.updateCardFlagIcon(saleId, cardFlagIcon)
    }.mapLeft {
        UpdateSalePaymentError.Unexpected(
            description = "Error while updating payment cardFlagIcon for sale $saleId",
            throwable = it,
        )
    }
}