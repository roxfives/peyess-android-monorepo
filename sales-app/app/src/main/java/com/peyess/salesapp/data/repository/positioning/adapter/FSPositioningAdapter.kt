package com.peyess.salesapp.data.repository.positioning.adapter

import com.peyess.salesapp.data.model.positioning.FSPositioning
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSPositioning.toPositioningDocument(): PositioningDocument {
    return PositioningDocument(
        id = id,
        storeId = storeId,
        storeIds = storeIds,
        prescriptionId = prescriptionId,
        takenByUid = takenByUid,
        serviceOrders = serviceOrders,
        patientUid = patientUid,
        patientDocument = patientDocument,
        patientName = patientName,
        eye = eye,
        baseLeft = baseLeft,
        baseLeftRotation = baseLeftRotation,
        baseRight = baseRight,
        baseRightRotation = baseRightRotation,
        baseTop = baseTop,
        baseBottom = baseBottom,
        topPointLength = topPointLength,
        topPointRotation = topPointRotation,
        bottomPointLength = bottomPointLength,
        bottomPointRotation = bottomPointRotation,
        bridgePivot = bridgePivot,
        checkBottom = checkBottom,
        checkTop = checkTop,
        checkLeft = checkLeft,
        checkLeftRotation = checkLeftRotation,
        checkMiddle = checkMiddle,
        checkRight = checkRight,
        checkRightRotation = checkRightRotation,
        framesBottom = framesBottom,
        framesLeft = framesLeft,
        framesRight = framesRight,
        framesTop = framesTop,
        opticCenterRadius = opticCenterRadius,
        opticCenterX = opticCenterX,
        opticCenterY = opticCenterY,
        proportionToPictureHorizontal = proportionToPictureHorizontal,
        proportionToPictureVertical = proportionToPictureVertical,
        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}