package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import java.time.ZonedDateTime

fun prescriptionFrom(
    clientName: String,
    clientDocument: String,
    clientUid: String,
    salespersonUid: String,

    storeId: String,

    serviceOrder: ServiceOrderDocument,
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

        lIpd = serviceOrder.lIpd,
        lBridge = serviceOrder.lBridge,
        lBridgeHoop = serviceOrder.lBridgeHoop,
        lHHoop = serviceOrder.lHorizontalHoop,
        lHe = serviceOrder.lHe,
        lVHoop = serviceOrder.lVerticalHoop,
        lDiameter = serviceOrder.lDiameter,

        lCylinder = prescriptionEntity.cylindricalLeft,
        lSpherical = prescriptionEntity.sphericalLeft,
        lAxisDegree = prescriptionEntity.axisLeft,

        hasPrism = prescriptionEntity.hasPrism,
        lPrismAxis = prescriptionEntity.prismAxisLeft,
        lPrismDegree = prescriptionEntity.prismDegreeLeft,
        lPrismPos = prescriptionEntity.prismPositionLeft,

        rIpd = serviceOrder.rIpd,
        rBridge = serviceOrder.rBridge,
        rBridgeHoop = serviceOrder.rBridgeHoop,
        rHHoop = serviceOrder.rHorizontalHoop,
        rHe = serviceOrder.rHe,
        rVHoop = serviceOrder.rVerticalHoop,
        rDiameter = serviceOrder.rDiameter,

        rCylinder = prescriptionEntity.cylindricalRight,
        rSpherical = prescriptionEntity.sphericalRight,
        rAxisDegree = prescriptionEntity.axisRight,
        rPrismAxis = prescriptionEntity.prismAxisRight,
        rPrismDegree = prescriptionEntity.prismDegreeRight,
        rPrismPos = prescriptionEntity.prismPositionRight,

        observation = prescriptionEntity.observation,

        createdBy = salespersonUid,
        createAllowedBy = salespersonUid,
        updatedBy = salespersonUid,
        updateAllowedBy = salespersonUid,
    )
}

