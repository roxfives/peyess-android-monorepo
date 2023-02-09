package com.peyess.salesapp.data.dao.prescription.error

sealed interface PrescriptionDaoError {
    val description: String
    val error: Throwable
}

sealed class ReadPrescriptionDaoError: PrescriptionDaoError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPrescriptionDaoError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPrescriptionDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}
