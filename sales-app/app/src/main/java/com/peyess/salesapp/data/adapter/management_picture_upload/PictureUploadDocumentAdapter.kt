package com.peyess.salesapp.data.adapter.management_picture_upload

import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadEntity

fun PictureUploadDocument.toPictureEntity(): PictureUploadEntity {
    return PictureUploadEntity(
        id = id,
        picture = picture,
        hasBeenUploaded = hasBeenUploaded,
        hasBeenDeleted = hasBeenDeleted,
        attemptCount = attemptCount,
        storagePath = storagePath,
        storageName = storageName,
        lastAttempt = lastAttempt,
    )
}