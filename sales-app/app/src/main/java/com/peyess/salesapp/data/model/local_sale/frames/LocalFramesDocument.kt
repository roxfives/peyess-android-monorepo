package com.peyess.salesapp.data.model.local_sale.frames

import com.peyess.salesapp.typing.frames.FramesType

data class LocalFramesDocument(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val description: String = "",
    val reference: String = "",
    val value: Double = 0.0,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
    val framesInfo: String = "",
)
