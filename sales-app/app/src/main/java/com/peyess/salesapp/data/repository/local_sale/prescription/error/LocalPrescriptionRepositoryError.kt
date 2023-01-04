package com.peyess.salesapp.data.repository.local_sale.prescription.error

sealed interface LocalPrescriptionRepositoryError {
    val message: String
    val error: Throwable?
}

sealed interface LocalPrescriptionResponseError: LocalPrescriptionRepositoryError
data class PrescriptionDataNotFound(
    override val message: String, override val error: Throwable? = null,
): LocalPrescriptionResponseError
data class PrescriptionPictureNotFound(
    override val message: String, override val error: Throwable? = null,
): LocalPrescriptionResponseError

data class Unexpected(
    override val message: String,
    override val error: Throwable? = null,
): LocalPrescriptionRepositoryError, LocalPrescriptionResponseError