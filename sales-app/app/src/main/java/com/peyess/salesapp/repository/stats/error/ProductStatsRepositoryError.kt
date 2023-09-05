package com.peyess.salesapp.repository.stats.error

interface ProductStatsRepositoryError {
    val description: String
    val error: Throwable
}

sealed class FetchProductStatsError: ProductStatsRepositoryError {
    data class NotFound(
        override val description: String = "Store not found",
        val throwable: Throwable? = null,

        override val error: Throwable = throwable ?: Throwable(description)
    ): FetchProductStatsError()

    data class DatabaseUnavailable(
        override val description: String = "Firestore not available",
        val throwable: Throwable? = null,

        override val error: Throwable = throwable ?: Throwable(description)
    ): FetchProductStatsError()

    data class Unexpected(
        override val description: String = "Store not found",
        val throwable: Throwable? = null,

        override val error: Throwable = throwable ?: Throwable(description)
    ): FetchProductStatsError()
}
