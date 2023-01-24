package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.service_order.utils.buildFilename
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

    val prefix = when(eye) {
        Eye.Left -> "LEFT_"
        Eye.Right -> "RIGHT_"
        Eye.None -> "NONE_"
    }

    return PictureUploadDocument(
        id = entryId,
        picture = picture,
        storagePath = storagePath,
        storageName = buildFilename(picture, prefix),
        hasBeenUploaded = false,
        hasBeenDeleted = false,
        attemptCount = 0,
        lastAttempt = ZonedDateTime.now(),
    )
}