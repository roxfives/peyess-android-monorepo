package com.peyess.salesapp.data.adapter.management_picture_upload

import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadEntity

fun PictureUploadEntity.toPictureDocument(): PictureUploadDocument {
    return PictureUploadDocument(
        id = id,
        picture = picture,

        fsDocumentPath = fsDocumentPath,
        fsDocumentField = fsDocumentField,

        storagePath = storagePath,
        storageName = storageName,

        hasBeenUploaded = hasBeenUploaded,
        hasBeenDeleted = hasBeenDeleted,

        attemptCount = attemptCount,
        lastAttempt = lastAttempt,
    )
}