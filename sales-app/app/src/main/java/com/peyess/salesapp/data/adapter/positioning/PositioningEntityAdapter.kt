package com.peyess.salesapp.data.adapter.positioning

import com.peyess.salesapp.data.adapter.raw_measuring.toLocalMeasuringDocument
import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.feature.sale.frames.state.Eye
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

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

fun PositioningEntity.toLocalMeasuringDocument(): LocalMeasuringDocument {
    val bottomPointBase = abs(bottomPointLength) * cos(abs(bottomPointRotation)) *
            proportionToPictureHorizontal
    val bottomPointHeight = abs(bottomPointLength) * sin(abs(bottomPointRotation)) *
            proportionToPictureVertical

    val topPointBase = abs(topPointLength) * cos(abs(topPointRotation)) *
            proportionToPictureHorizontal
    val topPointHeight = abs(topPointLength) * sin(abs(topPointRotation)) *
            proportionToPictureVertical

    val bridge = abs((if (eye == Eye.Right) framesRight else framesLeft) - bridgePivot) *
            proportionToPictureHorizontal
    val bridgeHelper = (if (eye == Eye.Right) framesRight else bridgePivot) +
            abs(bridgePivot - (if (eye == Eye.Right) framesRight else framesLeft)) / 2.0
    val virtualBridge = bridge / 2.0

    val diameter = 2.0 * opticCenterRadius * proportionToPictureHorizontal

    val horizontalHoop = abs(framesLeft - framesRight) * proportionToPictureHorizontal
    val horizontalBridgeHoop = bridge + horizontalHoop

    val horizontalCheck = abs(checkLeft - checkRight) * proportionToPictureHorizontal
    val verticalCheck = abs(checkTop - checkBottom) * proportionToPictureVertical
    val middleCheck = abs(checkMiddle - bridgeHelper) * proportionToPictureHorizontal

    val verticalHoop = abs(framesTop - framesBottom) * proportionToPictureVertical

    val ve = abs((if (eye == Eye.Right) framesRight else framesLeft) - opticCenterX) *
            proportionToPictureHorizontal
    val ipd = ve + virtualBridge
    val he = abs(opticCenterY - framesBottom) * proportionToPictureVertical
    val ho = abs(opticCenterX - (if (eye == Eye.Left) framesRight else framesLeft)) *
            proportionToPictureHorizontal
    val vu = abs(opticCenterY - framesTop) * proportionToPictureVertical

    val raw = RawMeasuring(
        eye = eye,

        baseSize = abs(baseRight - baseLeft),
        baseHeight = abs(baseBottom - baseTop),

        topAngle = topPointRotation,
        topPoint = hypot(topPointBase, topPointHeight),
        bottomAngle = bottomPointRotation,
        bottomPoint = hypot(bottomPointBase, bottomPointHeight),
        bridge = bridge,
        diameter = diameter,

        virtualBridge = virtualBridge,
        horizontalHoop = horizontalHoop,
        horizontalBridgeHoop = horizontalBridgeHoop,

        horizontalCheck = horizontalCheck,
        verticalCheck = verticalCheck,
        verticalHoop = verticalHoop,
        middleCheck = middleCheck,

        eulerAngleX = eulerAngleX,
        eulerAngleY = eulerAngleY,
        eulerAngleZ = eulerAngleZ,

        ve = ve,
        ipd = ipd,
        he = he,
        ho = ho,
        vu = vu,
    )

    return raw.toLocalMeasuringDocument()
}

fun PositioningEntity.toLocalPositioningDocument(): LocalPositioningDocument {
    return LocalPositioningDocument(
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
