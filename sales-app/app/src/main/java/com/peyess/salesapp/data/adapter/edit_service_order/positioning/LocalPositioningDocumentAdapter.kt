package com.peyess.salesapp.data.adapter.edit_service_order.positioning

import com.peyess.salesapp.data.model.edit_service_order.positioning.EditPositioningEntity
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument

fun LocalPositioningDocument.toEditPositioningEntity(): EditPositioningEntity {
    return EditPositioningEntity(
        id = id,
        soId = soId,
        eye = eye,
        picture = picture,
        rotation = rotation,
        device = device,
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
        realParamHeight = realParamHeight,
        realParamWidth = realParamWidth,
        proportionToPictureHorizontal = proportionToPictureHorizontal,
        proportionToPictureVertical = proportionToPictureVertical,
        eulerAngleX = eulerAngleX,
        eulerAngleY = eulerAngleY,
        eulerAngleZ = eulerAngleZ,
    )
}
