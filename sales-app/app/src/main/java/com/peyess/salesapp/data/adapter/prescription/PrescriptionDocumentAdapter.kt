package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.model.prescription.FSPrescription
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PrescriptionDocument.toFSPrescription(): FSPrescription {
    return FSPrescription(
        id = id,

        storeId = storeId,
        storeIds = storeIds,

        emitted = emitted.toTimestamp(),

        typeId = typeId,
        typeDesc = typeDesc,

        isCopy = isCopy,

        patientDocument = patientDocument,
        patientName = patientName,
        patientUid = patientUid,

        professionalDocument = professionalDocument,
        professionalName = professionalName,

        hasPrism = hasPrism,

        hasAddition = hasAddition,

        pd = pd,

        lIpd = lIpd,
        lCylinder = lCylinder,
        lSpherical = lSpherical,
        lAxisDegree = lAxisDegree,
        lAddition = lAddition,
        lPrismAxis = lPrismAxis,
        lPrismDegree = lPrismDegree,
        lPrismPos = lPrismPos.toName(),

        rIpd = rIpd,
        rCylinder = rCylinder,
        rSpherical = rSpherical,
        rAxisDegree = rAxisDegree,
        rAddition = rAddition,
        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = rPrismPos.toName(),

        docVersion = docVersion,
        isEditable = isEditable,

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}