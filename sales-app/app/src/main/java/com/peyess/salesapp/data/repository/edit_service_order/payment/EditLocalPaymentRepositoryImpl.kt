package com.peyess.salesapp.data.repository.edit_service_order.payment

import android.net.Uri
import arrow.core.Either
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.payment.toEditLocalPaymentEntity
import com.peyess.salesapp.data.adapter.edit_service_order.payment.toLocalPaymentDocument
import com.peyess.salesapp.data.dao.edit_service_order.payment.EditLocalPaymentDao
import com.peyess.salesapp.data.dao.local_client.LocalClientDao
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.InsertLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.ReadLocalPaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.UpdateLocalPaymentError
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import timber.log.Timber
import javax.inject.Inject

private const val attemptThreshold = 10L

class EditLocalPaymentRepositoryImpl @Inject constructor(
    private val editLocalPaymentDao: EditLocalPaymentDao,
    private val localClientDao: LocalClientDao,
): EditLocalPaymentRepository {
    override suspend fun addPayment(
        payment: LocalPaymentDocument,
    ): EditLocalPaymentInsertResponse = Either.catch {
        editLocalPaymentDao.addPayment(payment.toEditLocalPaymentEntity())
    }.mapLeft {
        InsertLocalPaymentError.Unexpected(
            description = "Error while inserting payment $payment",
            throwable = it,
        )
    }

    override suspend fun paymentsForSale(
        saleId: String,
    ): EditLocalPaymentFetchResponse = Either.catch {
        editLocalPaymentDao.paymentsForSale(saleId).map {
            val client = localClientDao.clientById(it.clientId)
            if (client == null) {
                error("Client ${it.clientId} not found for payment $it")
            }

            it.toLocalPaymentDocument(
                clientDocument = client.document,
                clientName = client.name,
                clientAddress = "${client.city}, ${client.state}",
            )
        }
    }.mapLeft {
        ReadLocalPaymentError.Unexpected(
            description = "Error while fetching payments for sale $saleId",
            throwable = it,
        )
    }

    override fun streamPaymentsForSale(
        saleId: String,
    ): EditLocalPaymentStreamResponse {
        return editLocalPaymentDao.streamPaymentsForSale(saleId)
            .map {
                it.map { payment ->
                    val client = localClientDao.clientById(payment.clientId)
                    if (client == null) {
                        error("Client ${payment.clientId} not found for payment $payment")
                    }

                    payment.toLocalPaymentDocument(
                        clientDocument = client.document,
                        clientName = client.name,
                        clientAddress = "${client.city}, ${client.state}",
                    )
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
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateClientId(saleId, clientId)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment clientId for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateMethodId(
        saleId: String,
        methodId: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateMethodId(saleId, methodId)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment methodId for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateMethodName(
        saleId: String,
        methodName: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateMethodName(saleId, methodName)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment methodName for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateMethodType(
        saleId: String,
        methodType: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateMethodType(saleId, methodType)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment methodType for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateValue(
        saleId: String,
        value: Double,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateValue(saleId, value)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment value for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateInstallments(
        saleId: String,
        installments: Int,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateInstallments(saleId, installments)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment installments for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateDocument(
        saleId: String,
        document: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateDocument(saleId, document)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment document for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateCardFlagName(
        saleId: String,
        cardFlagName: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateCardFlagName(saleId, cardFlagName)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment cardFlagName for sale $saleId",
            throwable = it,
        )
    }

    override suspend fun updateCardFlagIcon(
        saleId: String,
        cardFlagIcon: Uri,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateCardFlagIcon(saleId, cardFlagIcon)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment cardFlagIcon for sale $saleId",
            throwable = it,
        )
    }
}