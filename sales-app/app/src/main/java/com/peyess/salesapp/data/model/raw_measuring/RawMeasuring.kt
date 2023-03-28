package com.peyess.salesapp.data.model.raw_measuring

import com.peyess.salesapp.typing.general.Eye

data class RawMeasuring(
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

    val eulerAngleX: Double = 0.0,
    val eulerAngleY: Double = 0.0,
    val eulerAngleZ: Double = 0.0,
)
