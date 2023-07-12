package com.peyess.salesapp.data.dao.lenses.error

interface StoreLensDaoError {
    val description: String
    val error: Throwable
}

sealed class TotalLensesEnabledError: StoreLensDaoError {
    data class DatabaseUnavailable(
        override val description: String = "Firestore is unavailable",
        val throwable: Throwable? = null,
        override val error: Throwable = throwable ?: Throwable(description),
    ): TotalLensesEnabledError()

    data class StoreNotFound(
        override val description: String = "Current store was not found",
        val throwable: Throwable? = null,
        override val error: Throwable = throwable ?: Throwable(description),
    ): TotalLensesEnabledError()

    data class Unexpected(
        override val description: String = "Unexpected error while fetching total lenses enabled",
        val throwable: Throwable? = null,
        override val error: Throwable = throwable ?: Throwable(description),
    ): TotalLensesEnabledError()
}
