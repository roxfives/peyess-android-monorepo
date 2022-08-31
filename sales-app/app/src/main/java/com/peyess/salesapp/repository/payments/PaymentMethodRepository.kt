package com.peyess.salesapp.repository.payments

import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentMethodRepository {
    fun payments(): Flow<List<PaymentMethod>>
}