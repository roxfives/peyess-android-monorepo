package com.peyess.salesapp.data.repository.local_sale.payment.error

sealed interface SalePaymentRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface SalePaymentReadError: SalePaymentRepositoryError
data class SalePaymentNotFound(
    override val description: String,
    override val error: Throwable?,
): SalePaymentReadError

sealed interface SalePaymentWriteError: SalePaymentRepositoryError
data class SalePaymentWriteFailed(
    override val description: String,
    override val error: Throwable?,
): SalePaymentWriteError

data class Unexpected(
    override val description: String,
    override val error: Throwable?,
): SalePaymentRepositoryError, SalePaymentReadError, SalePaymentWriteError
