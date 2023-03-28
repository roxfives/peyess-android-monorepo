package com.peyess.salesapp.repository.payments.error

sealed interface PaymentMethodRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface PaymentMethodReadError: PaymentMethodRepositoryError
data class PaymentMethodNotFound(
    override val description: String,
    override val error: Throwable? = null,
): PaymentMethodReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): PaymentMethodRepositoryError, PaymentMethodReadError
