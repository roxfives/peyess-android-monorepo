package com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture.error

sealed interface PrescriptionPictureDownloadWorkerError {
    val description: String
    val error: Throwable
}

sealed class FetchPictureUriError: PrescriptionPictureDownloadWorkerError {
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

sealed class FetchPrescriptionError: PrescriptionPictureDownloadWorkerError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): FetchPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): FetchPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePictureUriError: PrescriptionPictureDownloadWorkerError {
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
