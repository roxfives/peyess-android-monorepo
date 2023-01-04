package com.peyess.salesapp.data.model.local_sale.measure

import com.peyess.salesapp.feature.sale.frames.state.Eye

data class LocalMeasuringDocument(
    val eye: Eye = Eye.None,

    val baseSize: Double = 0.0,
    val baseHeight: Double = 0.0,

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

    val fixedHorizontalBridgeHoop: Double = 0.0,
    val fixedBridge: Double = 0.0,
    val fixedIpd: Double = 0.0,
    val fixedHHoop: Double = 0.0,
    val fixedHe: Double = 0.0,
    val fixedVHoop: Double = 0.0,

    val fixedDiameter: Double = 0.0,

    val eulerAngleX: Double = 0.0,
    val eulerAngleY: Double = 0.0,
    val eulerAngleZ: Double = 0.0,
)
