package com.peyess.salesapp.screen.edit_service_order.payment.adapter

import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument
import com.peyess.salesapp.feature.payment.model.Frames

fun LocalFramesDocument.toFrames(): Frames {
    return Frames(
        soId = soId,
        areFramesNew = areFramesNew,
        design = design,
        reference = reference,
        value = value,
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}