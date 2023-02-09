package com.peyess.salesapp.data.repository.local_sale.payment.error

sealed interface LocalPaymentRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalPaymentReadError: LocalPaymentRepositoryError
data class LocalPaymentNotFound(
    override val description: String,
    override val error: Throwable? = null,
): LocalPaymentReadError

sealed interface LocalPaymentWriteError: LocalPaymentRepositoryError
data class LocalPaymentWriteFailed(
    override val description: String,
    override val error: Throwable? = null,
): LocalPaymentWriteError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): LocalPaymentRepositoryError, LocalPaymentReadError, LocalPaymentWriteError
