package com.peyess.salesapp.data.repository.measuring.adapter

import com.peyess.salesapp.data.model.measuring.FSMeasuring
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSMeasuring.toMeasuringDocument(): MeasuringDocument {
    return MeasuringDocument(
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
        eye = Eye.toEye(eye),
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
        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
