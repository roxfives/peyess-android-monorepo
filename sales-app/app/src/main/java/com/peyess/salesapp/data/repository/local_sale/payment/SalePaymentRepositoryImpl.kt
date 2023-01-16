package com.peyess.salesapp.data.repository.local_sale.payment

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.local_sale.payment.toSalePaymentDocument
import com.peyess.salesapp.data.adapter.local_sale.payment.toSalePaymentEntity
import com.peyess.salesapp.data.dao.local_sale.payment.SalePaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentNotFound
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.Unexpected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SalePaymentRepositoryImpl @Inject constructor(
    private val salePaymentDao: SalePaymentDao,
): SalePaymentRepository {
    override suspend fun paymentForSale(saleId: String): SalePaymentResponse = Either.catch {
        salePaymentDao
            .paymentsForSale(saleId)
            .map { it.toSalePaymentDocument() }
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override fun watchPaymentsForSale(saleId: String): Flow<SalePaymentResponse> {
        return salePaymentDao.watchPaymentsForSale(saleId)
            .map {
                it.map { payment ->
                    payment.toSalePaymentDocument()
                }.right() as SalePaymentResponse
            }.catch {
                val error = Unexpected(
                    description = it.message ?: "",
                    error = it,
                )

                emit(error.left())
            }
    }

    override fun watchPayment(paymentId: Long): Flow<SinglePaymentResponse> {
        return salePaymentDao.watchPayment(paymentId)
            .map {
                it?.toSalePaymentDocument()?.right()
                    ?: (SalePaymentNotFound(
                        description = "Payment with id $paymentId not found",
                    ) as SalePaymentReadError).left()
            }.catch {
                val error = Unexpected(
                    description = it.message ?: "",
                    error = it,
                )

                emit(error.left())
            }
    }

    override fun watchTotalPayment(saleId: String): Flow<SalePaymentTotalResponse> {
        return salePaymentDao.watchTotalPayment(saleId)
            .map { it.right() as SalePaymentTotalResponse }
            .catch {
                val error = Unexpected(
                    description = it.message ?: "",
                    error = it,
                )

                emit(error.left())
            }
    }

    override suspend fun payment(paymentId: Long): SinglePaymentResponse = Either.catch {
        salePaymentDao
            .payment(paymentId)
            ?.toSalePaymentDocument()
    }.mapLeft {
        Unexpected(
            description = it.message ?: "Error while reading payment $paymentId",
            error = it,
        )
    }.leftIfNull {
        SalePaymentNotFound(description = "Payment $paymentId not found")
    }

    override suspend fun totalPaymentForSale(
        saleId: String,
    ): SalePaymentTotalResponse = Either.catch {
        salePaymentDao.totalPaymentForSale(saleId)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override suspend fun addPaymentToSale(
        paymentDocument: SalePaymentDocument,
    ): SalePaymentWriteResult = Either.catch {
        val entity = paymentDocument.toSalePaymentEntity()

        salePaymentDao.add(entity)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override suspend fun updatePayment(
        paymentDocument: SalePaymentDocument,
    ): SalePaymentUpdateResult = Either.catch {
        val entity = paymentDocument.toSalePaymentEntity()

        salePaymentDao.update(entity)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override suspend fun deletePayment(
        paymentDocument: SalePaymentDocument,
    ): SalePaymentDeleteResult = Either.catch {
        val entity = paymentDocument.toSalePaymentEntity()

        salePaymentDao.delete(entity)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }
}
