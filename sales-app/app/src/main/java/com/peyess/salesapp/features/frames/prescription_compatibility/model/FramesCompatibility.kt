package com.peyess.salesapp.features.frames.prescription_compatibility.model

import com.peyess.salesapp.typing.frames.FramesType

data class FramesCompatibility(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val description: String = "",
    val reference: String = "",
    val value: Double = 0.0,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
    val framesInfo: String = "",
)
