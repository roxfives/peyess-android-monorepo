package com.peyess.salesapp.feature.sale.service_order.model

import com.peyess.salesapp.dao.sale.frames.FramesType

data class Frames(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val description: String = "",
    val reference: String = "",
    val value: Double = 0.0,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
)
