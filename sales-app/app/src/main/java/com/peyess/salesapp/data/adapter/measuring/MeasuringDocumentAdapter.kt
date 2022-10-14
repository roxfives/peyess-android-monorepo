package com.peyess.salesapp.data.adapter.measuring

import com.peyess.salesapp.data.model.measuring.FSMeasuring
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.utils.time.toTimestamp

fun MeasuringDocument.toFSMeasuring(): FSMeasuring {
    return FSMeasuring(
        id = id,

        storeId = storeId,
        storeIds = storeIds,

        prescriptionId = prescriptionId,
        positioningId = positioningId,

        takenByUid = takenByUid,

        serviceOrders = serviceOrders,

        correctionModelVersion = correctionModelVersion,
        patientUid = patientUid,
        patientDocument = patientDocument,
        patientName = patientName,
        eye = eyeName(eye),
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

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}

private fun eyeName(eye: Eye): String {
    return when(eye) {
        is Eye.Left -> "left"
        is Eye.Right -> "right"
        is Eye.None -> "none"
    }
}