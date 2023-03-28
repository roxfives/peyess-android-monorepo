package com.peyess.salesapp.feature.payment.model

import com.peyess.salesapp.typing.frames.FramesType

data class Frames(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val design: String = "",
    val reference: String = "",
    val value: Double = 0.0,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
    val framesInfo: String = "",
) {
    val name = "$design, $reference ($tagCode)"
}
