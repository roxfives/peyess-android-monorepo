package com.peyess.salesapp.data.repository.lenses.error

interface StoreLensesRepositoryError {
    val description: String
    val error: Throwable
}

sealed class TotalLensesRepositoryError: StoreLensesRepositoryError {
    data class DatabaseUnavailable(
        override val description: String = "Firestore is unavailable",
        val throwable: Throwable? = null,
        override val error: Throwable = throwable ?: Throwable(description),
    ): TotalLensesRepositoryError()

    data class StoreNotFound(
        override val description: String = "Current store was not found",
        val throwable: Throwable? = null,
        override val error: Throwable = throwable ?: Throwable(description),
    ): TotalLensesRepositoryError()

    data class Unexpected(
        override val description: String = "Unexpected error while fetching total lenses enabled",
        val throwable: Throwable? = null,
        override val error: Throwable = throwable ?: Throwable(description),
    ): TotalLensesRepositoryError()
}