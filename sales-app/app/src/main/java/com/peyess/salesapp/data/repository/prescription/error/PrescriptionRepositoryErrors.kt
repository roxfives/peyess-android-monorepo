package com.peyess.salesapp.data.repository.prescription.error

sealed interface PrescriptionRepositoryError {
    val description: String
    val error: Throwable
}

sealed class ReadPrescriptionRepositoryError: PrescriptionRepositoryError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPrescriptionRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPrescriptionRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class GetPictureUrlPrescriptionRepositoryError: PrescriptionRepositoryError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): GetPictureUrlPrescriptionRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): GetPictureUrlPrescriptionRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePrescriptionRepositoryError: PrescriptionRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdatePrescriptionRepositoryError() {
        override val error = throwable ?: Throwable(description)
    }
}
