package com.peyess.salesapp.repository.payments

import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun payments(): Flow<List<PaymentMethod>>
}