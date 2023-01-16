package com.peyess.salesapp.repository.payments

import com.peyess.salesapp.data.model.payment_method.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentMethodRepository {
    fun payments(): Flow<List<PaymentMethod>>
}