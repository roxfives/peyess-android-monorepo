package com.peyess.salesapp.features.edit_service_order.updater.adapter

import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument
import java.time.ZonedDateTime

fun LocalPositioningDocument.toPositioningUpdateDocument(
    collaboratorUid: String,
    patientUid: String,
    patientDocument: String,
    patientName: String,
    updated: ZonedDateTime,
): PositioningUpdateDocument {
    return PositioningUpdateDocument(
        takenByUid = collaboratorUid,
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
        updated = updated,
        updatedBy = collaboratorUid,
        updateAllowedBy = collaboratorUid,
    )
}