package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.adapter.internal.utils.prismPositionName
import com.peyess.salesapp.data.model.prescription.FSPrescription
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PrescriptionDocument.toFSPrescription(): FSPrescription {
    return FSPrescription(
        id = id,

        emitted = emitted.toTimestamp(),

        picture = picture,

        typeId = typeId,
        typeDesc = typeDesc,

        isCopy = isCopy,

        patientDocument = patientDocument,
        patientName = patientName,
        professionalDocument = professionalDocument,
        professionalName = professionalName,

        pd = pd,
        lIpd = lIpd,
        rIpd = rIpd,

        lCylinder = lCylinder,
        lSpherical = lSpherical,
        lAxisDegree = lAxisDegree,

        rCylinder = rCylinder,
        rSpherical = rSpherical,
        rAxisDegree = rAxisDegree,

        hasAddition = hasAddition,
        lAddition = lAddition,
        rAddition = rAddition,

        hasPrism = hasPrism,

        lPrismAxis = lPrismAxis,
        lPrismDegree = lPrismDegree,
        lPrismPos = prismPositionName(lPrismPos),

        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = prismPositionName(rPrismPos),

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}