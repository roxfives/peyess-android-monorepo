package com.peyess.salesapp.features.edit_service_order.updater.adapter

import android.net.Uri
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import java.time.ZonedDateTime

fun PrescriptionUpdateDocument.toPictureUploadDocument(
    entryId: Long = 0L,
    storeId: String,
    prescriptionId: String,
    pictureUri: Uri,
    salesApplication: SalesApplication,
): PictureUploadDocument {
    val storagePath = salesApplication
        .getString(R.string.storage_client_prescription)
        .format(storeId, patientUid, prescriptionId)

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