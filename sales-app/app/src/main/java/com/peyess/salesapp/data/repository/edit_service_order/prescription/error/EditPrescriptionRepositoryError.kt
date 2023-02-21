package com.peyess.salesapp.data.repository.edit_service_order.prescription.error

sealed interface EditPrescriptionRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertPrescriptionError: EditPrescriptionRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePrescriptionError: EditPrescriptionRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdatePrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadPrescriptionError: EditPrescriptionRepositoryError {
    data class PrescriptionNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class DeletePrescriptionError: EditPrescriptionRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): DeletePrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}
