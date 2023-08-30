package com.peyess.salesapp.data.repository.local_sale.frames.adapter

import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument

fun FramesEntity.toFramesDocument(): FramesDocument {
    return FramesDocument(
        soId = soId,
        areFramesNew = areFramesNew,
        design = design,
        reference = reference,
        value = value.toBigDecimal(),
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}
