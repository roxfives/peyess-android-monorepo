package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import java.time.ZonedDateTime

fun prescriptionFrom(
    id: String,

    clientName: String,
    clientDocument: String,
    clientUid: String,
    salespersonUid: String,

    storeId: String,

    dataEntity: PrescriptionDataEntity,
    pictureEntity: PrescriptionPictureEntity,
): PrescriptionDocument {
    return PrescriptionDocument(
        id = id,

        storeId = storeId,
        storeIds = listOf(storeId),

        emitted = ZonedDateTime.now(),

        picture = pictureEntity.pictureUri.toString(),
        isCopy = pictureEntity.isCopy,

        patientUid = clientUid,
        patientDocument = clientDocument,
        patientName = clientName,

        professionalDocument = pictureEntity.professionalId,
        professionalName = pictureEntity.professionalName,

        hasAddition = dataEntity.hasAddition,
        lAddition = dataEntity.additionLeft,
        rAddition = dataEntity.additionRight,

        lCylinder = dataEntity.cylindricalLeft,
        lSpherical = dataEntity.sphericalLeft,
        lAxisDegree = dataEntity.axisLeft,

        hasPrism = dataEntity.hasPrism,
        lPrismAxis = dataEntity.prismAxisLeft,
        lPrismDegree = dataEntity.prismDegreeLeft,
        lPrismPos = dataEntity.prismPositionLeft,

        rCylinder = dataEntity.cylindricalRight,
        rSpherical = dataEntity.sphericalRight,
        rAxisDegree = dataEntity.axisRight,
        rPrismAxis = dataEntity.prismAxisRight,
        rPrismDegree = dataEntity.prismDegreeRight,
        rPrismPos = dataEntity.prismPositionRight,

        createdBy = salespersonUid,
        createAllowedBy = salespersonUid,
        updatedBy = salespersonUid,
        updateAllowedBy = salespersonUid,
    )
}

