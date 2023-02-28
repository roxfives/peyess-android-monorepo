package com.peyess.salesapp.features.edit_service_order.updater.adapter

import android.net.Uri
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument
import java.time.ZonedDateTime

fun PositioningUpdateDocument.toPictureUploadDocument(
    entryId: Long = 0L,
    storeId: String,
    positioningId: String,
    pictureUri: Uri,
    salesApplication: SalesApplication,
): PictureUploadDocument {
    val storagePath = salesApplication
        .getString(R.string.storage_client_positioning)
        .format(storeId, patientUid, positioningId)

    val storageFilename = salesApplication
        .getString(R.string.storage_client_positioning_filename)

    return PictureUploadDocument(
        id = entryId,
        picture = pictureUri,
        storagePath = storagePath,
        storageName = storageFilename,
        hasBeenUploaded = false,
        hasBeenDeleted = false,
        attemptCount = 0,
        lastAttempt = ZonedDateTime.now(),
    )
}