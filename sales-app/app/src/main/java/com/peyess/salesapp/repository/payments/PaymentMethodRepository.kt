package com.peyess.salesapp.repository.payments

import arrow.core.Either
import com.peyess.salesapp.data.model.payment_method.PaymentMethodDocument
import com.peyess.salesapp.repository.payments.error.PaymentMethodReadError

typealias PaymentMethodsResponse = Either<PaymentMethodReadError, List<PaymentMethodDocument>>

interface PaymentMethodRepository {
    suspend fun payments(): PaymentMethodsResponse
}