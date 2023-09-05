package com.peyess.salesapp.dao.stats.error

sealed interface LensStatsDaoError {
    val description: String
    val error: Throwable
}

sealed class FetchLensStatsDaoError: LensStatsDaoError {
    data class NotFound(
        override val description: String = "Store not found",
        val throwable: Throwable? = null,

        override val error: Throwable = throwable ?: Throwable(description)
    ): FetchLensStatsDaoError()

    data class DatabaseUnavailable(
        override val description: String = "Firestore not available",
        val throwable: Throwable? = null,

        override val error: Throwable = throwable ?: Throwable(description)
    ): FetchLensStatsDaoError()

    data class Unexpected(
        override val description: String = "Store not found",
        val throwable: Throwable? = null,

        override val error: Throwable = throwable ?: Throwable(description)
    ): FetchLensStatsDaoError()
}


