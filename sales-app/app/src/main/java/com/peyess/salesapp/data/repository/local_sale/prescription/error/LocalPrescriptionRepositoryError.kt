package com.peyess.salesapp.data.repository.local_sale.prescription.error

sealed interface LocalPrescriptionRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalPrescriptionResponseError: LocalPrescriptionRepositoryError
data class PrescriptionDataNotFound(
    override val description: String, override val error: Throwable? = null,
): LocalPrescriptionResponseError
data class PrescriptionPictureNotFound(
    override val description: String, override val error: Throwable? = null,
): LocalPrescriptionResponseError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): LocalPrescriptionRepositoryError, LocalPrescriptionResponseError