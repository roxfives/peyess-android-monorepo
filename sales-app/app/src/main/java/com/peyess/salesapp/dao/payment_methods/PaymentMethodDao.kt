package com.peyess.salesapp.dao.payment_methods

import kotlinx.coroutines.flow.Flow

interface PaymentMethodDao {
    fun payments(): Flow<List<PaymentMethod>>
}