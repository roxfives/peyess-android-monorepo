package com.peyess.salesapp.data.repository.local_sale.frames.adapter

import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument

fun FramesDocument.toFramesEntity(): FramesEntity {
    return FramesEntity(
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
