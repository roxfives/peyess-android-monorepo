package com.peyess.salesapp.data.dao.payment_method

import com.peyess.salesapp.data.model.payment_method.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentMethodDao {
    fun payments(): Flow<List<PaymentMethod>>
}