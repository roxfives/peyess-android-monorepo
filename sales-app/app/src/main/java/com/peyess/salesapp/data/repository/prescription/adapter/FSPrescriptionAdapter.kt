package com.peyess.salesapp.data.repository.prescription.adapter

import com.peyess.salesapp.data.model.prescription.FSPrescription
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSPrescription.toPrescriptionDocument(): PrescriptionDocument {
    return PrescriptionDocument(
        id = id,
        storeId = storeId,
        storeIds = storeIds,
        emitted = emitted.toZonedDateTime(),
        typeId = typeId,
        typeDesc = typeDesc,
        isCopy = isCopy,
        lensTypeCategoryId = lensTypeCategoryId,
        lensTypeCategory = LensTypeCategoryName.fromName(lensTypeCategory),
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
        lPrismPos = PrismPosition.toPrism(lPrismPos),
        rIpd = rIpd,
        rCylinder = rCylinder,
        rSpherical = rSpherical,
        rAxisDegree = rAxisDegree,
        rAddition = rAddition,
        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = PrismPosition.toPrism(rPrismPos),
        observation = observation,
        docVersion = docVersion,
        isEditable = isEditable,
        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
