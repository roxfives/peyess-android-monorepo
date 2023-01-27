package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.feature.sale.service_order.model.Prescription
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

fun LocalPrescriptionDocument.toPrescription(): Prescription {
    return Prescription(
        id = id,
        soId = soId,
        pictureUri = pictureUri,
        professionalName = professionalName,
        professionalId = professionalId,
        isCopy = isCopy,
        prescriptionDate = prescriptionDate,
        sphericalLeft = sphericalLeft,
        sphericalRight = sphericalRight,
        cylindricalLeft = cylindricalLeft,
        cylindricalRight = cylindricalRight,
        axisLeft = axisLeft,
        axisRight = axisRight,
        hasAddition = hasAddition,
        additionLeft = additionLeft,
        additionRight = additionRight,
        hasPrism = hasPrism,
        prismDegreeLeft = prismDegreeLeft,
        prismDegreeRight = prismDegreeRight,
        prismAxisLeft = prismAxisLeft,
        prismAxisRight = prismAxisRight,
        prismPositionLeft = prismPositionLeft,
        prismPositionRight = prismPositionRight,
    )
}