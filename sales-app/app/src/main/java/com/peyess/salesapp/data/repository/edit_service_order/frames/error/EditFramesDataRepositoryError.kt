package com.peyess.salesapp.data.repository.edit_service_order.frames.error

sealed interface EditFramesDataRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertFramesError: EditFramesDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertFramesError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateFramesError: EditFramesDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdateFramesError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadFramesError: EditFramesDataRepositoryError {
    data class FramesNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadFramesError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadFramesError() {
        override val error = throwable ?: Throwable(description)
    }
}
