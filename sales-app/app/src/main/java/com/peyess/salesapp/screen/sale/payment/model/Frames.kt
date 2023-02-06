package com.peyess.salesapp.screen.sale.payment.model

import com.peyess.salesapp.typing.frames.FramesType

data class Frames(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val description: String = "",
    val reference: String = "",
    val value: Double = 0.0,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
    val framesInfo: String = "",
) {
    val name = "$description, $reference ($tagCode)"
}
