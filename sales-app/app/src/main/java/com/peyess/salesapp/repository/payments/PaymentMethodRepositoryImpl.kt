package com.peyess.salesapp.repository.payments

import arrow.core.Either
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.payment_methods.PaymentMethodDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias PaymentMethodsResponse = Either<Throwable, List<PaymentMethod>>

class PaymentMethodRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentMethodDao,
): PaymentMethodRepository {

    override fun payments(): Flow<List<PaymentMethod>> {
        return paymentDao.payments()
    }
}