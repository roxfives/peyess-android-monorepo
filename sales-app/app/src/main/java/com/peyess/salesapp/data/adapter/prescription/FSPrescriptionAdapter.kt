package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import java.time.ZonedDateTime

fun prescriptionFrom(
    clientName: String,
    clientDocument: String,
    clientUid: String,
    salespersonUid: String,

    storeId: String,

    prescriptionEntity: PrescriptionEntity,
): PrescriptionDocument {
    return PrescriptionDocument(
        id = prescriptionEntity.id,

        storeId = storeId,
        storeIds = listOf(storeId),

        emitted = ZonedDateTime.now(),

        isCopy = prescriptionEntity.isCopy,

        patientUid = clientUid,
        patientDocument = clientDocument,
        patientName = clientName,

        professionalDocument = prescriptionEntity.professionalId,
        professionalName = prescriptionEntity.professionalName,

        lensTypeCategoryId = prescriptionEntity.lensTypeCategoryId,
        lensTypeCategory = prescriptionEntity.lensTypeCategory,

        hasAddition = prescriptionEntity.hasAddition,
        lAddition = prescriptionEntity.additionLeft,
        rAddition = prescriptionEntity.additionRight,

        lCylinder = prescriptionEntity.cylindricalLeft,
        lSpherical = prescriptionEntity.sphericalLeft,
        lAxisDegree = prescriptionEntity.axisLeft,

        hasPrism = prescriptionEntity.hasPrism,
        lPrismAxis = prescriptionEntity.prismAxisLeft,
        lPrismDegree = prescriptionEntity.prismDegreeLeft,
        lPrismPos = prescriptionEntity.prismPositionLeft,

        rCylinder = prescriptionEntity.cylindricalRight,
        rSpherical = prescriptionEntity.sphericalRight,
        rAxisDegree = prescriptionEntity.axisRight,
        rPrismAxis = prescriptionEntity.prismAxisRight,
        rPrismDegree = prescriptionEntity.prismDegreeRight,
        rPrismPos = prescriptionEntity.prismPositionRight,

        createdBy = salespersonUid,
        createAllowedBy = salespersonUid,
        updatedBy = salespersonUid,
        updateAllowedBy = salespersonUid,
    )
}

