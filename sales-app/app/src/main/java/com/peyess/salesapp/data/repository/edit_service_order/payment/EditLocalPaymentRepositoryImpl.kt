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
import com.peyess.salesapp.navigation.edit_service_order.service_order.editServiceOrderRoute
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import timber.log.Timber
import java.math.BigDecimal
import java.time.ZonedDateTime
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
        val total = editLocalPaymentDao.totalPaid(saleId)

        total?.toBigDecimal() ?: BigDecimal.ZERO
    }.mapLeft {
        ReadLocalPaymentError.Unexpected(
            description = "Error while fetching total paid for sale $saleId",
            throwable = it,
        )
    }

    override fun streamTotalPaid(saleId: String): EditLocalPaymentStreamTotalResponse {
        return editLocalPaymentDao.streamTotalPaid(saleId)
            .map {
                val result = it?.toBigDecimal() ?: BigDecimal.ZERO

                result.right()
            }
    }

    override suspend fun paymentsForSale(
        saleId: String,
    ): EditLocalPaymentFetchResponse = Either.catch {
        editLocalPaymentDao.paymentsForSale(saleId).map {
            val client = localClientDao.clientById(it.clientId)
            if (client == null) {
                Timber.w("Client ${it.clientId} not found for payment $it")
            }

            it.toLocalPaymentDocument(
                clientDocument = client?.document ?: "",
                clientName = client?.name ?: "",
                clientAddress = "${client?.city}, ${client?.state}",
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
                        Timber.e("Client ${payment.clientId} not found for payment $payment")
                    }

                    payment.toLocalPaymentDocument(
                        clientDocument = client?.document ?: "",
                        clientName = client?.name ?: "",
                        clientAddress = "${client?.city}, ${client?.state}",
                    )
                }.right()
            }
    }

    override suspend fun paymentById(
        paymentId: Long,
        clientId: String,
    ): EditLocalPaymentFetchSingleResponse = Either.catch {
        val client = localClientDao.clientById(clientId)
        if (client == null) {
            Timber.w("Client $clientId not found for payment $paymentId")
        }

        editLocalPaymentDao.paymentById(paymentId)?.toLocalPaymentDocument(
            clientDocument = client?.document ?: "",
            clientName = client?.name ?: "",
            clientAddress = "${client?.city}, ${client?.state}",
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

            if (it != null) {
                if (client == null) {
                    Timber.w("Client $clientId not found for payment $it")
                }

                it.toLocalPaymentDocument(
                    clientDocument = client?.document ?: "",
                    clientName = client?.name ?: "",
                    clientAddress = "${client?.city}, ${client?.state}",
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
        value: BigDecimal,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateValue(paymentId, value.toDouble())
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

    override suspend fun updateDueDateData(
        paymentId: Long,
        dueDateMode: PaymentDueDateMode,
        dueDatePeriod: Int,
        dueDate: ZonedDateTime,
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateDaysToDueDate(
            paymentId,
            dueDateMode,
            dueDatePeriod,
            dueDate,
        )
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment days to due date for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateLegalId(
        paymentId: Long,
        legalId: String
    ): EditLocalPaymentUpdateResponse = Either.catch {
        editLocalPaymentDao.updateLegalId(paymentId, legalId)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment legalId for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun updateHasLegalId(
        paymentId: Long,
        hasLegalId: Boolean
    ): EditLocalPaymentUpdateResponse = Either.catch {
        val hasLegalIdAsInt = if (hasLegalId) 1 else 0

        editLocalPaymentDao.updateHasLegalId(paymentId, hasLegalIdAsInt)
    }.mapLeft {
        UpdateLocalPaymentError.Unexpected(
            description = "Error while updating payment hasLegalId for sale $paymentId",
            throwable = it,
        )
    }

    override suspend fun deletePaymentsForSale(
        saleId: String,
    ): EditLocalPaymentDeleteResponse = Either.catch {
        editLocalPaymentDao.deletePaymentsForSale(saleId)
    }.mapLeft {
        DeleteLocalPaymentError.Unexpected(
            description = "Error while deleting payments for sale $saleId",
            throwable = it,
        )
    }
}
