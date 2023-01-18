package com.peyess.salesapp.data.repository.management_picture_upload.error

sealed interface PictureUploadRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface PictureUploadReadError: PictureUploadRepositoryError
data class PictureUploadFetchError(
    override val description: String,
    override val error: Throwable? = null,
): PictureUploadReadError

sealed interface PictureUploadWriteError: PictureUploadRepositoryError
data class PictureUploadUpdateError(
    override val description: String,
    override val error: Throwable? = null,
): PictureUploadWriteError

data class UnexpectedPictureError(
    override val description: String,
    override val error: Throwable? = null,
): PictureUploadRepositoryError, PictureUploadReadError
