package com.peyess.salesapp.data.repository.local_sale.frames.model

import com.peyess.salesapp.typing.frames.FramesType
import java.math.BigDecimal

data class FramesDocument(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val design: String = "",
    val reference: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
    val framesInfo: String = "",
)
