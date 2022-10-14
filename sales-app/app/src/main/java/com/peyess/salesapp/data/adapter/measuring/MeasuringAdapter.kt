package com.peyess.salesapp.data.adapter.measuring

import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.Measuring

fun Measuring.toMeasuringDocument(
    documentId: String,
    storeId: String,
    prescriptionId: String,
    positioningId: String,
    salespersonId: String,
    clientId: String,
    clientDocument: String,
    clientName: String,
    soId: String,
): MeasuringDocument {
    return MeasuringDocument(
        id = documentId,

        storeId = storeId,
        storeIds = listOf(storeId),

        prescriptionId = prescriptionId,
        positioningId = positioningId,

        takenByUid = salespersonId,

        serviceOrders = listOf(soId),

        correctionModelVersion = "v0.1",

        patientUid = clientId,
        patientDocument = clientDocument,
        patientName = clientName,

        eye = eye,

        baseSize = baseSize,
        topAngle = topAngle,
        topPoint = topPoint,
        bottomAngle = bottomAngle,
        bottomPoint = bottomPoint,
        bridge = bridge,
        diameter = diameter,

        verticalCheck = verticalCheck,
        verticalHoop = verticalHoop,
        horizontalBridgeHoop = horizontalBridgeHoop,
        horizontalCheck = horizontalCheck,
        horizontalHoop = horizontalHoop,
        middleCheck = middleCheck,

        eulerAngleX = eulerAngleX,
        eulerAngleY = eulerAngleY,
        eulerAngleZ = eulerAngleZ,

        ipd = ipd,
        he = he,
        ho = ho,
        ve = ve,
        vu = vu,

        fixedBridge = fixedBridge,
        fixedIpd = fixedIpd,
        fixedHHoop = fixedHHoop,
        fixedHe = fixedHe,
        fixedVHoop = fixedVHoop,

        fixedDiameter = fixedDiameter,

        createdBy = salespersonId,
        createAllowedBy = salespersonId,
        updatedBy = salespersonId,
        updateAllowedBy = salespersonId,
    )
}