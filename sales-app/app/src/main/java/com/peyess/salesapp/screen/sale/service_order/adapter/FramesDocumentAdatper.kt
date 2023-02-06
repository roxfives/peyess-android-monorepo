package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.screen.sale.service_order.model.Frames

fun FramesDocument.toFrames(): Frames {
    return Frames(
        soId = soId,
        areFramesNew = areFramesNew,
        description = description,
        reference = reference,
        value = value,
        tagCode = tagCode,
        type = type,
    )
}
