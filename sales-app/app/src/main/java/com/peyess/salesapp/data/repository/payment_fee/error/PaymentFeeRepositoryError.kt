package com.peyess.salesapp.data.repository.payment_fee.error

sealed interface PaymentFeeRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface PaymentFeeReadError: PaymentFeeRepositoryError
data class PaymentFeeNotFound(
    override val description: String,
    override val error: Throwable? = null,
): PaymentFeeReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): PaymentFeeRepositoryError, PaymentFeeReadError