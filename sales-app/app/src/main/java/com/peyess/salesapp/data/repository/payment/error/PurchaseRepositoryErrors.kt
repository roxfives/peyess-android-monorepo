package com.peyess.salesapp.data.repository.payment.error

sealed interface PurchaseRepositoryError {
    val description: String
    val error: Throwable
}

sealed class PurchaseRepositoryPaginationError: PurchaseRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): PurchaseRepositoryPaginationError() {
        override val error = throwable ?: Throwable(description)
    }
}
