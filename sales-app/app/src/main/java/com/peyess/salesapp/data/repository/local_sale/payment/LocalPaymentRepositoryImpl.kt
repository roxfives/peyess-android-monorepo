package com.peyess.salesapp.data.repository.local_sale.payment

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.local_sale.payment.toLocalPaymentDocument
import com.peyess.salesapp.data.adapter.local_sale.payment.toLocalPaymentEntity
import com.peyess.salesapp.data.dao.local_sale.payment.LocalPaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.repository.local_sale.payment.error.LocalPaymentNotFound
import com.peyess.salesapp.data.repository.local_sale.payment.error.LocalPaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.Unexpected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

class LocalPaymentRepositoryImpl @Inject constructor(
    private val localPaymentDao: LocalPaymentDao,
): LocalPaymentRepository {
    override suspend fun paymentForSale(saleId: String): LocalPaymentResponse = Either.catch {
        localPaymentDao
            .paymentsForSale(saleId)
            .map { it.toLocalPaymentDocument() }
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override fun watchPaymentsForSale(saleId: String): Flow<LocalPaymentResponse> {
        return localPaymentDao.watchPaymentsForSale(saleId)
            .map {
                it.map { payment ->
                    payment.toLocalPaymentDocument()
                }.right() as LocalPaymentResponse
            }.catch {
                val error = Unexpected(
                    description = it.message ?: "",
                    error = it,
                )

                emit(error.left())
            }
    }

    override fun watchPayment(paymentId: Long): Flow<SinglePaymentResponse> {
        return localPaymentDao.watchPayment(paymentId)
            .map {
                it?.toLocalPaymentDocument()?.right()
                    ?: (LocalPaymentNotFound(
                        description = "Payment with id $paymentId not found",
                    ) as LocalPaymentReadError).left()
            }.catch {
                val error = Unexpected(
                    description = it.message ?: "",
                    error = it,
                )

                emit(error.left())
            }
    }

    override fun watchTotalPayment(saleId: String): Flow<LocalPaymentTotalResponse> {
        return localPaymentDao.watchTotalPayment(saleId)
            .map {
                val value = it?.toBigDecimal() ?: BigDecimal.ZERO

                value.right()
            }.catch<LocalPaymentTotalResponse> {
                Timber.e(
                    message = "Error while watching total payment for sale $saleId: ${it.message}",
                    t = it,
                )

                val error = Unexpected(
                    description = it.message ?: "",
                    error = it,
                )

                emit(error.left())
            }
    }

    override suspend fun payment(paymentId: Long): SinglePaymentResponse = Either.catch {
        localPaymentDao
            .payment(paymentId)
            ?.toLocalPaymentDocument()
    }.mapLeft {
        Unexpected(
            description = it.message ?: "Error while reading payment $paymentId",
            error = it,
        )
    }.leftIfNull {
        LocalPaymentNotFound(description = "Payment $paymentId not found")
    }

    override suspend fun totalPaymentForSale(
        saleId: String,
    ): LocalPaymentTotalResponse = Either.catch {
        localPaymentDao.totalPaymentForSale(saleId)?.toBigDecimal() ?: BigDecimal.ZERO
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override suspend fun addPaymentToSale(
        paymentDocument: LocalPaymentDocument,
    ): LocalPaymentWriteResult = Either.catch {
        val entity = paymentDocument.toLocalPaymentEntity()

        localPaymentDao.add(entity)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override suspend fun updatePayment(
        paymentDocument: LocalPaymentDocument,
    ): LocalPaymentUpdateResult = Either.catch {
        val entity = paymentDocument.toLocalPaymentEntity()

        localPaymentDao.update(entity)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override suspend fun deletePayment(
        paymentDocument: LocalPaymentDocument,
    ): LocalPaymentDeleteResult = Either.catch {
        val entity = paymentDocument.toLocalPaymentEntity()

        localPaymentDao.delete(entity)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }
}
