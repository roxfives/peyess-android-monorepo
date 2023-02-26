package com.peyess.salesapp.data.adapter.positioning

import com.peyess.salesapp.data.model.positioning.FSPositioningUpdate
import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PositioningUpdateDocument.toFSPositioningUpdate(): FSPositioningUpdate {
    return FSPositioningUpdate(
        takenByUid = takenByUid,
        patientUid = patientUid,
        patientDocument = patientDocument,
        patientName = patientName,
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
        device = device,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
