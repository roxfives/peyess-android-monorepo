package com.peyess.salesapp.data.repository.edit_service_order.positioning.error

sealed interface EditPositioningRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertPositioningError: EditPositioningRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertPositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePositioningError: EditPositioningRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdatePositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadPositioningError: EditPositioningRepositoryError {
    data class PositioningNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPositioningError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class DeletePositioningError: EditPositioningRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): DeletePositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}
