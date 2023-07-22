package com.peyess.salesapp.features.edit_service_order.updater.adapter

import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import java.time.ZonedDateTime

fun LocalPrescriptionDocument.toPrescriptionUpdateDocument(
    client: LocalClientDocument,
    serviceOrder: ServiceOrderDocument,
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
        lIpd = serviceOrder.lIpd,
        lBridge = serviceOrder.lBridge,
        lBridgeHoop = serviceOrder.lBridgeHoop,
        lHHoop = serviceOrder.lHorizontalHoop,
        lHe = serviceOrder.lHe,
        lVHoop = serviceOrder.lVerticalHoop,
        lDiameter = serviceOrder.lDiameter,
        lCylinder = cylindricalLeft,
        lSpherical = sphericalLeft,
        lAxisDegree = axisLeft,
        lAddition = additionLeft,
        lPrismAxis = prismAxisLeft,
        lPrismDegree = prismDegreeLeft,
        lPrismPos = prismPositionLeft.toName(),
        rIpd = serviceOrder.rIpd,
        rBridge = serviceOrder.rBridge,
        rBridgeHoop = serviceOrder.rBridgeHoop,
        rHHoop = serviceOrder.rHorizontalHoop,
        rHe = serviceOrder.rHe,
        rVHoop = serviceOrder.rVerticalHoop,
        rDiameter = serviceOrder.rDiameter,
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
