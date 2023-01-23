package com.peyess.salesapp.utils.cloud.storage.error

sealed interface StorageError {
    val description: String
    val error: Throwable?
}

sealed interface StorageUploadError: StorageError
data class FileNotFound(
    override val description: String = "File not found",
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorObjectNotFound(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorBucketNotFound(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorProjectNotFound(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorQuotaExceeded(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorNotAuthenticated(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorNotAuthorized(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorRetryLimitExceeded(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorInvalidChecksum(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError
data class StorageUnexpectedErrorCanceled(
    override val description: String,
    override val error: Throwable? = null,
): StorageUploadError

data class StorageUnexpectedError(
    override val description: String,
    override val error: Throwable? = null,
): StorageError, StorageUploadError