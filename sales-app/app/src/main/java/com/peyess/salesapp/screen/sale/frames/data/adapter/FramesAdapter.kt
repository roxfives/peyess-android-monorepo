package com.peyess.salesapp.screen.sale.frames.data.adapter

import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.screen.sale.frames.data.model.Frames
import com.peyess.salesapp.features.frames.prescription_compatibility.model.FramesCompatibility

fun Frames.toFramesDocument(): FramesDocument {
    return FramesDocument(
        soId = soId,
        areFramesNew = areFramesNew,
        description = description,
        reference = reference,
        value = value,
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}

fun Frames.toFramesCompatibility(): FramesCompatibility {
    return FramesCompatibility(
        soId = soId,
        areFramesNew = areFramesNew,
        description = description,
        reference = reference,
        value = value,
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}