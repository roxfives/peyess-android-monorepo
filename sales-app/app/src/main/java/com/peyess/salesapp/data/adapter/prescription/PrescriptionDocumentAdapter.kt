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

        lensTypeCategoryId = lensTypeCategoryId,
        lensTypeCategory = lensTypeCategory.toName(),

        hasPrism = hasPrism,

        hasAddition = hasAddition,

        pd = pd,

        lIpd = lIpd,
        lBridge = lBridge,
        lBridgeHoop = lBridgeHoop,
        lHHoop = lHHoop,
        lHe = lHe,
        lVHoop = lVHoop,
        lDiameter = lDiameter,

        lCylinder = lCylinder,
        lSpherical = lSpherical,
        lAxisDegree = lAxisDegree,
        lAddition = lAddition,
        lPrismAxis = lPrismAxis,
        lPrismDegree = lPrismDegree,
        lPrismPos = lPrismPos.toName(),

        rIpd = rIpd,
        rBridge = rBridge,
        rBridgeHoop = rBridgeHoop,
        rHHoop = rHHoop,
        rHe = rHe,
        rVHoop = rVHoop,
        rDiameter = rDiameter,

        rCylinder = rCylinder,
        rSpherical = rSpherical,
        rAxisDegree = rAxisDegree,
        rAddition = rAddition,
        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = rPrismPos.toName(),

        observation = observation,

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