package com.peyess.salesapp.feature.sale.service_order.adapter

import android.net.Uri
import androidx.core.net.toFile
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private fun buildFilename(uri: Uri): String {
    val fileName = DateTimeFormatter
        .ofPattern("yyyyMMdd_HHmmss")
        .format(ZonedDateTime.now())
    val fileExtension = try {
        uri.toFile().extension
    } catch (e: Exception) {
        "jpeg"
    }

    return "$fileName.$fileExtension"
}

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

    return PictureUploadDocument(
        id = entryId,
        picture = pictureUri,
        storagePath = storagePath,
        storageName = buildFilename(pictureUri),
        hasBeenUploaded = false,
        hasBeenDeleted = false,
        attemptCount = 0,
        lastAttempt = ZonedDateTime.now(),
    )
}
