package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.feature.sale.frames.state.Eye
import java.time.ZonedDateTime

fun LocalPositioningDocument.toPictureUploadDocument(
    salesApplication: SalesApplication,
    storeId: String,
    clientId: String,
    positioningId: String,
    entryId: Long = 0L,
): PictureUploadDocument {
    val storagePath = salesApplication
        .getString(R.string.storage_client_positioning)
        .format(storeId, clientId, positioningId)

    val storageFilename = salesApplication
        .getString(R.string.storage_client_positioning_filename)

    return PictureUploadDocument(
        id = entryId,
        picture = picture,
        storagePath = storagePath,
        storageName = storageFilename,
        hasBeenUploaded = false,
        hasBeenDeleted = false,
        attemptCount = 0,
        lastAttempt = ZonedDateTime.now(),
    )
}