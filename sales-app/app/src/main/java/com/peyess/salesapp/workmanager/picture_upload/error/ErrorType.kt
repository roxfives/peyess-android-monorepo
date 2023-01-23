package com.peyess.salesapp.workmanager.picture_upload.error

import com.peyess.salesapp.data.repository.management_picture_upload.error.PictureUploadRepositoryError
import com.peyess.salesapp.utils.cloud.storage.error.StorageUploadError

sealed class ErrorType {
    data class UploadError(val error: StorageUploadError): ErrorType()
    data class LocalError(val error: PictureUploadRepositoryError): ErrorType()
    object StorageNotInitialized: ErrorType()
}
