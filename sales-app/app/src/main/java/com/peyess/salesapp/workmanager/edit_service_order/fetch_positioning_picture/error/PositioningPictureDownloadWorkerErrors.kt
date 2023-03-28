package com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture.error

sealed interface PositioningPictureDownloadWorkerError {
    val description: String
    val error: Throwable
}

sealed class FetchPictureUriError: PositioningPictureDownloadWorkerError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): FetchPictureUriError() {
        override val error = throwable ?: Throwable(description)
    }
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): FetchPictureUriError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class FetchPositioningError: PositioningPictureDownloadWorkerError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): FetchPositioningError() {
        override val error = throwable ?: Throwable(description)
    }
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): FetchPositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePictureUriError: PositioningPictureDownloadWorkerError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdatePictureUriError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdatePictureUriError() {
        override val error = throwable ?: Throwable(description)
    }
}
