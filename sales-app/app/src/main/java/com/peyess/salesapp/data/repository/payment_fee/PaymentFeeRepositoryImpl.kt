package com.peyess.salesapp.data.repository.payment_fee

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.data.adapter.payment_fee.toPaymentFeeDocument
import com.peyess.salesapp.data.adapter.payment_fee.toPaymentFeeEntity
import com.peyess.salesapp.data.dao.payment_fee.PaymentFeeDao
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.payment_fee.error.PaymentFeeNotFound
import com.peyess.salesapp.data.repository.payment_fee.error.Unexpected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class PaymentFeeRepositoryImpl @Inject constructor(
    private val paymentFeeDao: PaymentFeeDao,
) : PaymentFeeRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val currentPaymentFeeForSale: MutableMap<String, Flow<PaymentFeeDocument>> = mutableMapOf()

    override fun watchPaymentFeeForSale(saleId: String): Flow<PaymentFeeDocument> {
        return currentPaymentFeeForSale[saleId]
            ?: createWatcherForSale(saleId)
    }

    override suspend fun getPaymentFeeForSale(saleId: String): PaymentFeeDocument? {
        return paymentFeeDao
            .getPaymentFee(saleId)
            ?.toPaymentFeeDocument()
    }

    override suspend fun updatePaymentFeeForSale(paymentFee: PaymentFeeDocument) {
        val entity = paymentFee.toPaymentFeeEntity()

        Timber.i("Updating paymentFee $entity from $paymentFee")

        paymentFeeDao.updatePaymentFee(entity)
    }

    override suspend fun paymentFeeForSale(
        saleId: String,
    ): PaymentFeeRepositoryResponse = Either.catch {
        paymentFeeDao
            .getPaymentFee(saleId)
            ?.toPaymentFeeDocument()
    }.mapLeft {
        Unexpected(
            description = it.message
                ?: "Unexpected error while getting payment fee for sale $saleId",
            error = it,
        )
    }.leftIfNull {
        PaymentFeeNotFound(
            description = "PaymentFee for sale $saleId not found",
        )
    }

    private fun createWatcherForSale(saleId: String): Flow<PaymentFeeDocument> {
        Timber.i("Creating flow for sale $saleId")

        val flow = paymentFeeDao
            .watchPaymentFee(saleId)
            .map { it?.toPaymentFeeDocument() ?: PaymentFeeDocument() }
            .shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )

        currentPaymentFeeForSale[saleId] = flow
        return flow
    }
}