package com.peyess.salesapp.feature.service_order.model

import com.peyess.salesapp.typing.frames.FramesType
import java.math.BigDecimal

data class Frames(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val design: String = "",
    val reference: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
) {
    val name = "$design, $reference ($tagCode)"
}
