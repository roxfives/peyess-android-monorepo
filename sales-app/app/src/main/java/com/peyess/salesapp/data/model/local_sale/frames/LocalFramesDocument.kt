package com.peyess.salesapp.data.model.local_sale.frames

import com.peyess.salesapp.typing.frames.FramesType
import java.math.BigDecimal

data class LocalFramesDocument(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val design: String = "",
    val reference: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
    val framesInfo: String = "",
)
