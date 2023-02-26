package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.model.prescription.FSPrescriptionUpdate
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PrescriptionUpdateDocument.toFSPrescriptionUpdate(): FSPrescriptionUpdate {
    return FSPrescriptionUpdate(
        emitted = emitted.toTimestamp(),
        typeId = typeId,
        typeDesc = typeDesc,
        isCopy = isCopy,
        patientUid = patientUid,
        patientDocument = patientDocument,
        patientName = patientName,
        professionalDocument = professionalDocument,
        professionalName = professionalName,
        hasPrism = hasPrism,
        hasAddition = hasAddition,
        lCylinder = lCylinder,
        lSpherical = lSpherical,
        lAxisDegree = lAxisDegree,
        lAddition = lAddition,
        lPrismAxis = lPrismAxis,
        lPrismDegree = lPrismDegree,
        lPrismPos = lPrismPos,
        rCylinder = rCylinder,
        rSpherical = rSpherical,
        rAxisDegree = rAxisDegree,
        rAddition = rAddition,
        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = rPrismPos,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
