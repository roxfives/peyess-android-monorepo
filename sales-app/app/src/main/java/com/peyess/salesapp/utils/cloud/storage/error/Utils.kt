package com.peyess.salesapp.utils.cloud.storage.error

import com.google.firebase.storage.StorageException


private fun storageUploadError(error: StorageException): StorageUploadError {
    val errorCode = error.errorCode
    val errorMessage = error.message ?: "<empty error message>"

    return when (errorCode) {
        errorObjectNotFound ->
            StorageUnexpectedErrorObjectNotFound(
                description = errorMessage,
                error = error,
            )
        errorBucketNotFound ->
            StorageUnexpectedErrorBucketNotFound(
                description = errorMessage,
                error = error,
            )
        errorProjectNotFound ->
            StorageUnexpectedErrorProjectNotFound(
                description = errorMessage,
                error = error,
            )
        errorQuotaExceeded ->
            StorageUnexpectedErrorQuotaExceeded(
                description = errorMessage,
                error = error,
            )
        errorNotAuthenticated ->
            StorageUnexpectedErrorNotAuthenticated(
                description = errorMessage,
                error = error,
            )
        errorNotAuthorized ->
            StorageUnexpectedErrorNotAuthorized(
                description = errorMessage,
                error = error,
            )
        errorRetryLimitExceeded ->
            StorageUnexpectedErrorRetryLimitExceeded(
                description = errorMessage,
                error = error,
            )
        errorInvalidChecksum ->
            StorageUnexpectedErrorInvalidChecksum(
                description = errorMessage,
                error = error,
            )
        errorCanceled ->
            StorageUnexpectedErrorCanceled(
                description = errorMessage,
                error = error,
            )
//        errorUnknown
        else ->
            StorageUnexpectedError(
                description = errorMessage,
                error = error,
            )
    }
}

fun uploadErrorAdapter(error: Throwable): StorageUploadError {
    return if (error !is StorageException) {
        StorageUnexpectedError(
            description = "Unexpected error while uploading file",
            error = error,
        )
    } else {
        storageUploadError(error)
    }
}
