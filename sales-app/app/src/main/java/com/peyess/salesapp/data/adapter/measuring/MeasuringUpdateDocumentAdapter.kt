package com.peyess.salesapp.data.adapter.measuring

import com.peyess.salesapp.data.model.measuring.FSMeasuringUpdate
import com.peyess.salesapp.data.model.measuring.MeasuringUpdateDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun MeasuringUpdateDocument.toFSMeasuringUpdate(): FSMeasuringUpdate {
    return FSMeasuringUpdate(
        takenByUid = takenByUid,
        patientUid = patientUid,
        patientDocument = patientDocument,
        patientName = patientName,
        baseSize = baseSize,
        baseHeight = baseHeight,
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
        fixedHorizontalBridgeHoop = fixedHorizontalBridgeHoop,
        fixedBridge = fixedBridge,
        fixedIpd = fixedIpd,
        fixedHHoop = fixedHHoop,
        fixedHe = fixedHe,
        fixedVHoop = fixedVHoop,
        fixedDiameter = fixedDiameter,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
