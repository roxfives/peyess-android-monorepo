package com.peyess.salesapp.data.repository.positioning.error

sealed interface PositioningRepositoryError {
    val description: String
    val error: Throwable
}

sealed class ReadPositioningRepositoryError: PositioningRepositoryError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPositioningRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPositioningRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePositioningRepositoryError: PositioningRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdatePositioningRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}
