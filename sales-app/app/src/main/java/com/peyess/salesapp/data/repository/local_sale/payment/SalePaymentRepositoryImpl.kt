package com.peyess.salesapp.data.repository.local_sale.payment

import arrow.core.Either
import com.peyess.salesapp.data.adapter.local_sale.payment.toSalePaymentDocument
import com.peyess.salesapp.data.adapter.local_sale.payment.toSalePaymentEntity
import com.peyess.salesapp.data.dao.local_sale.payment.SalePaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.Unexpected
import javax.inject.Inject

class SalePaymentRepositoryImpl @Inject constructor(
    private val salePaymentDao: SalePaymentDao,
): SalePaymentRepository {
    override fun paymentForSale(saleId: String): SalePaymentResponse = Either.catch {
        salePaymentDao
            .paymentsForSale(saleId)
            .map { it.toSalePaymentDocument() }
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override fun totalPaymentForSale(saleId: String): SalePaymentTotalResponse = Either.catch {
        salePaymentDao.totalPaymentForSale(saleId)
    }.mapLeft {
        Unexpected(
            description = it.message ?: "",
            error = it,
        )
    }

    override fun addPaymentToSale(
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

    override fun updatePayment(
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
}
