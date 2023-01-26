package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import java.time.ZonedDateTime

fun LocalPrescriptionDocument.toPictureUploadDocument(
    salesApplication: SalesApplication,
    storeId: String,
    clientId: String,
    prescriptionId: String,
    entryId: Long = 0L,
): PictureUploadDocument {
    val storagePath = salesApplication
        .getString(R.string.storage_client_prescription)
        .format(storeId, clientId, prescriptionId)

    val storageFilename = salesApplication
        .getString(R.string.storage_client_prescription_filename)

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
