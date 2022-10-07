package com.peyess.salesapp.data.adapter.positioning

import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import java.time.ZonedDateTime

fun PositioningEntity.toPositioningDocument(
    id: String,

    storeId: String,
    prescriptionId: String,

    patientUid: String,
    patientDocument: String,
    patientName: String,

    salesPersonId: String,

    takenByUid: String,
    soId: String,
): PositioningDocument {
    return PositioningDocument(
        id = id,

        storeId = storeId,
        storeIds = listOf(storeId),
        prescriptionId = prescriptionId,

        picture = picture.toString(),

        eye = eye.toName(),

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

        takenByUid = takenByUid,
        serviceOrders = listOf(soId),
        patientUid = patientUid,
        patientDocument = patientDocument,
        patientName = patientName,

        created = ZonedDateTime.now(),
        createdBy = salesPersonId,
        createAllowedBy = salesPersonId,

        updated = ZonedDateTime.now(),
        updatedBy = salesPersonId,
        updateAllowedBy = salesPersonId,
    )
}
