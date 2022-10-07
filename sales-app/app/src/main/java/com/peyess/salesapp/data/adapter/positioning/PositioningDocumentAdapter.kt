package com.peyess.salesapp.data.adapter.positioning

import com.google.firebase.Timestamp
import com.peyess.salesapp.data.model.positioning.FSPositioning
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.utils.time.toTimestamp


fun PositioningDocument.toFSPositioning(): FSPositioning {
    return FSPositioning(
        id = id,
        storeId = storeId,
        storeIds = storeIds,
        prescriptionId = prescriptionId,
        picture = picture,
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

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}