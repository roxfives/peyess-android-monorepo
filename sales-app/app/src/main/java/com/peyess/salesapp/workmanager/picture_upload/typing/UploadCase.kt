package com.peyess.salesapp.workmanager.picture_upload.typing

sealed class UploadCase {
    object UploadPending: UploadCase()
    object AlreadyUploaded: UploadCase()
    object UploadFailedPreviously: UploadCase()
    object UploadFailedTooManyTimes: UploadCase()
}
