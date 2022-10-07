package com.peyess.salesapp.feature.sale.lens_pick.model

import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class Measuring(
    val eye: Eye = Eye.None,

    val baseSize: Double = 0.0,

    val topAngle: Double = 0.0,
    val topPoint: Double = 0.0,
    val bottomAngle: Double = 0.0,
    val bottomPoint: Double = 0.0,
    val bridge: Double = 0.0,
    val diameter: Double = 0.0,
    val virtualBridge: Double = 0.0,

    val verticalCheck: Double = 0.0,
    val verticalHoop: Double = 0.0,
    val horizontalBridgeHoop: Double = 0.0,
    val horizontalCheck: Double = 0.0,
    val horizontalHoop: Double = 0.0,
    val middleCheck: Double = 0.0,

    val ipd: Double = 0.0, // dnp
    val he: Double = 0.0,
    val ho: Double = 0.0,
    val ve: Double = 0.0,
    val vu: Double = 0.0,

    val fixedBridge: Double = 0.0,
    val fixedIpd: Double = 0.0,
    val fixedHHoop: Double = 0.0,
    val fixedHe: Double = 0.0,
    val fixedVHoop: Double = 0.0,

    val eulerAngleX: Double = 0.0,
    val eulerAngleY: Double = 0.0,
    val eulerAngleZ: Double = 0.0,
)

fun PositioningEntity.toMeasuring(): Measuring {
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

    return Measuring(
        eye = eye,

        baseSize = abs(baseRight - baseLeft),

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

        ve = ve,
        ipd = ipd,
        he = he,
        ho = ho,
        vu = vu,

        fixedBridge = -1.0,
        fixedIpd = -1.0,
        fixedHHoop = -1.0,
        fixedHe = -1.0,
        fixedVHoop = -1.0,
    )
}