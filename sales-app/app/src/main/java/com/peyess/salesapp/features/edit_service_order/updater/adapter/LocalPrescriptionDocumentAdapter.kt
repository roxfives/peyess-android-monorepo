package com.peyess.salesapp.features.edit_service_order.updater.adapter

import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import java.time.ZonedDateTime

fun LocalPrescriptionDocument.toPrescriptionUpdateDocument(
    client: LocalClientDocument,
    collaboratorUid: String,
    updated: ZonedDateTime,
): PrescriptionUpdateDocument {
    return PrescriptionUpdateDocument(
        emitted = prescriptionDate,
        typeId = "",
        typeDesc = "",
        lensTypeCategoryId = lensTypeCategoryId,
        lensTypeCategory = lensTypeCategory,
        isCopy = isCopy,
        patientUid = client.id,
        patientDocument = client.document,
        patientName = client.name,
        professionalDocument = professionalId,
        professionalName = professionalName,
        hasPrism = hasPrism,
        hasAddition = hasAddition,
        lCylinder = cylindricalLeft,
        lSpherical = sphericalLeft,
        lAxisDegree = axisLeft,
        lAddition = additionLeft,
        lPrismAxis = prismAxisLeft,
        lPrismDegree = prismDegreeLeft,
        lPrismPos = prismPositionLeft.toName(),
        rCylinder = cylindricalRight,
        rSpherical = sphericalRight,
        rAxisDegree = axisRight,
        rAddition = additionRight,
        rPrismAxis = prismAxisRight,
        rPrismDegree = prismDegreeRight,
        rPrismPos = prismPositionRight.toName(),
        observation = observation,
        updated = updated,
        updatedBy = collaboratorUid,
        updateAllowedBy = collaboratorUid,
    )
}
