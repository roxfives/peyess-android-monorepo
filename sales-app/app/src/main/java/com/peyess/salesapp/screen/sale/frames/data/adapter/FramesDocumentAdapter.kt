package com.peyess.salesapp.screen.sale.frames.data.adapter

import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.screen.sale.frames.data.model.Frames

fun FramesDocument.toFrames(): Frames {
    return Frames(
        soId = soId,
        areFramesNew = areFramesNew,
        description = design,
        reference = reference,
        value = value,
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}
