package com.peyess.salesapp.repository.payments

import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.payment_methods.PaymentMethodDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentMethodDao,
): PaymentRepository {
    override fun payments(): Flow<List<PaymentMethod>> {
        return paymentDao.payments()
    }

}