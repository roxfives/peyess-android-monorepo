package com.peyess.salesapp.repository.payments

import arrow.core.Either
import com.peyess.salesapp.data.model.payment_method.PaymentMethod
import com.peyess.salesapp.data.dao.payment_method.PaymentMethodDao
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