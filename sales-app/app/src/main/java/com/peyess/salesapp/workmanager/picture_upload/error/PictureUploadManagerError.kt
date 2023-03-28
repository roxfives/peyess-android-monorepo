package com.peyess.salesapp.workmanager.picture_upload.error

sealed interface PictureUploadManagerError {
    val errorType: ErrorType
    val description: String
    val error: Throwable?
}

data class StorageNotInitialized(
    override val description: String = "Storage not initialized",
    override val error: Throwable? = null,
): PictureUploadManagerError {
    override val errorType: ErrorType = ErrorType.StorageNotInitialized
}

data class UploadFailed(
    override val errorType: ErrorType,
    override val description: String,
    override val error: Throwable? = null,
): PictureUploadManagerError

data class LocalOperationFailed(
    override val errorType: ErrorType,
    override val description: String,
    override val error: Throwable? = null,
): PictureUploadManagerError