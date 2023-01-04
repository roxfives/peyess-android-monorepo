package com.peyess.salesapp.data.adapter.raw_measuring

import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.data.model.raw_measuring.RawMeasuring
import com.peyess.salesapp.feature.sale.lens_pick.model.Measuring
import com.peyess.salesapp.features.measuring_correction.CorrectionField
import com.peyess.salesapp.features.measuring_correction.correctionModelFactory

fun RawMeasuring.toMeasuring(): Measuring {
    val fixerBridgeHoop = correctionModelFactory(CorrectionField.HorizontalBridgeHoop)
    val fixerBridge = correctionModelFactory(CorrectionField.Bridge)
    val fixerIpd = correctionModelFactory(CorrectionField.IPD)
    val fixerHHoop = correctionModelFactory(CorrectionField.HHoop)
    val fixerHe = correctionModelFactory(CorrectionField.HE)
    val fixerVHoop = correctionModelFactory(CorrectionField.VHoop)
    val fixerDiameter = correctionModelFactory(CorrectionField.Diameter)

    return Measuring(
        eye = eye,

        baseSize = baseSize,
        baseHeight = baseHeight,

        topAngle = topAngle,
        topPoint = topPoint,
        bottomAngle = bottomAngle,
        bottomPoint = bottomPoint,
        bridge = bridge,
        diameter = diameter,
        virtualBridge = virtualBridge,

        verticalCheck = verticalCheck,
        verticalHoop = verticalHoop,
        horizontalBridgeHoop = horizontalBridgeHoop,
        horizontalCheck = horizontalCheck,
        horizontalHoop = horizontalHoop,
        middleCheck = middleCheck,

        ipd = ipd,
        he = he,
        ho = ho,
        ve = ve,
        vu = vu,

        fixedHorizontalBridgeHoop =  fixerBridgeHoop.fixField(this),
        fixedBridge = fixerBridge.fixField(this),
        fixedIpd = fixerIpd.fixField(this),
        fixedHHoop = fixerHHoop.fixField(this),
        fixedHe = fixerHe.fixField(this),
        fixedVHoop = fixerVHoop.fixField(this),

        fixedDiameter = fixerDiameter.fixField(this),

        eulerAngleX = eulerAngleX,
        eulerAngleY = eulerAngleY,
        eulerAngleZ = eulerAngleZ,
    )
}

fun RawMeasuring.toLocalMeasuringDocument(): LocalMeasuringDocument {
    val fixerBridgeHoop = correctionModelFactory(CorrectionField.HorizontalBridgeHoop)
    val fixerBridge = correctionModelFactory(CorrectionField.Bridge)
    val fixerIpd = correctionModelFactory(CorrectionField.IPD)
    val fixerHHoop = correctionModelFactory(CorrectionField.HHoop)
    val fixerHe = correctionModelFactory(CorrectionField.HE)
    val fixerVHoop = correctionModelFactory(CorrectionField.VHoop)
    val fixerDiameter = correctionModelFactory(CorrectionField.Diameter)

    return LocalMeasuringDocument(
        eye = eye,

        baseSize = baseSize,
        baseHeight = baseHeight,

        topAngle = topAngle,
        topPoint = topPoint,
        bottomAngle = bottomAngle,
        bottomPoint = bottomPoint,
        bridge = bridge,
        diameter = diameter,
        virtualBridge = virtualBridge,

        verticalCheck = verticalCheck,
        verticalHoop = verticalHoop,
        horizontalBridgeHoop = horizontalBridgeHoop,
        horizontalCheck = horizontalCheck,
        horizontalHoop = horizontalHoop,
        middleCheck = middleCheck,

        ipd = ipd,
        he = he,
        ho = ho,
        ve = ve,
        vu = vu,

        fixedHorizontalBridgeHoop =  fixerBridgeHoop.fixField(this),
        fixedBridge = fixerBridge.fixField(this),
        fixedIpd = fixerIpd.fixField(this),
        fixedHHoop = fixerHHoop.fixField(this),
        fixedHe = fixerHe.fixField(this),
        fixedVHoop = fixerVHoop.fixField(this),

        fixedDiameter = fixerDiameter.fixField(this),

        eulerAngleX = eulerAngleX,
        eulerAngleY = eulerAngleY,
        eulerAngleZ = eulerAngleZ,
    )
}
