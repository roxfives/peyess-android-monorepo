package com.peyess.salesapp.data.repository.measuring.error

sealed interface MeasuringRepositoryErrors {
    val description: String
    val error: Throwable
}

sealed class ReadMeasuringRepositoryError: MeasuringRepositoryErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadMeasuringRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadMeasuringRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateMeasuringRepositoryError: MeasuringRepositoryErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateMeasuringRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}
