package com.peyess.salesapp.data.repository.edit_service_order.payment

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.payment.toEditLocalPaymentEntity
import com.peyess.salesapp.data.adapter.edit_service_order.payment.toLocalPaymentDocument
import com.peyess.salesapp.data.dao.edit_service_order.payment.EditLocalPaymentDao
import com.peyess.salesapp.data.dao.local_client.LocalClientDao
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.DeleteLocalPaymentError
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

    override suspend fun deletePayment(
        paymentId: Long,
    ): EditLocalPaymentDeleteResponse = Either.catch {
        editLocalPaymentDao.deletePayment(paymentId)
    }.mapLeft {
        DeleteLocalPaymentError.Unexpected(
            description = "Error while deleting payment $paymentId",
            throwable = it,
        )
    }

    override suspend fun totalPaid(
        saleId: String,
    ): EditLocalPaymentFetchTotalResponse = Either.catch {
        editLocalPaymentDao.totalPaid(saleId) ?: 0.0
    }.mapLeft {
        ReadLocalPaymentError.Unexpected(
            description = "Error while fetching total paid for sale $saleId",
            throwable = it,
        )
    }

    override fun streamTotalPaid(saleId: String): EditLocalPaymentStreamTotalResponse {
        return editLocalPaymentDao.streamTotalPaid(saleId)
            .map { (it ?: 0.0).right() }
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

    override suspend fun paymentById(
        paymentId: Long,
        clientId: String,
    ): EditLocalPaymentFetchSingleResponse = Either.catch {
        val client = localClientDao.clientById(clientId)
        if (client == null) {
            error("Client $clientId not found for payment $paymentId")
        }

        editLocalPaymentDao.paymentById(paymentId)?.toLocalPaymentDocument(
            clientDocument = client.document,
            clientName = client.name,
            clientAddress = "${client.city}, ${client.state}",
        )
    }.mapLeft {
        ReadLocalPaymentError.Unexpected(
            description = "Error while fetching payment $paymentId",
            throwable = it,
        )
    }.leftIfNull {
        ReadLocalPaymentError.NotFound(
            description = "Payment $paymentId not found",
        )
    }

    override fun streamPaymentById(
        paymentId: Long,
        clientId: String,
    ): EditLocalPaymentStreamSingleResponse {
        return editLocalPaymentDao.streamPaymentById(paymentId).map {
            val client = localClientDao.clientById(clientId)

            if (client == null) {
                ReadLocalPaymentError.Unexpected(
                    description = "Client $clientId not found for payment $it",
                ).left()
            } else if (it != null) {
                it.toLocalPaymentDocument(
                    clientDocument = client.document,
                    clientName = client.name,
                    clientAddress = "${client.city}, ${client.state}",
                ).right()
            } else {
                ReadLocalPaymentError.NotFound(
                    description = "Payment $paymentId not found",
                ).left()
            }
        }
    }

    override suspend fun updateClientId(
        paymentId: Long,
        clientId: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateClientId(paymentId, clientId)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment clientId for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateMethodId(
        paymentId: Long,
        methodId: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateMethodId(paymentId, methodId)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment methodId for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateMethodName(
        paymentId: Long,
        methodName: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateMethodName(paymentId, methodName)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment methodName for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateMethodType(
        paymentId: Long,
        methodType: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateMethodType(paymentId, methodType)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment methodType for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateValue(
        paymentId: Long,
        value: Double,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateValue(paymentId, value)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment value for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateInstallments(
        paymentId: Long,
        installments: Int,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateInstallments(paymentId, installments)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment installments for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateDocument(
        paymentId: Long,
        document: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateDocument(paymentId, document)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment document for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateCardFlagName(
        paymentId: Long,
        cardFlagName: String,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateCardFlagName(paymentId, cardFlagName)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment cardFlagName for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateCardFlagIcon(
        paymentId: Long,
        cardFlagIcon: Uri,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateCardFlagIcon(paymentId, cardFlagIcon)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment cardFlagIcon for sale $paymentId",
            throwable = it,
        )
    }
}