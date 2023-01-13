package com.peyess.salesapp.data.repository.local_sale.frames.model

import com.peyess.salesapp.dao.sale.frames.FramesType

data class FramesDocument(
    val soId: String = "",
    val areFramesNew: Boolean = false,
    val description: String = "",
    val reference: String = "",
    val value: Double = 0.0,
    val tagCode: String = "",
    val type: FramesType = FramesType.None,
)
