package com.peyess.salesapp.data.repository.local_sale.frames.adapter

import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.utils.extentions.roundToDouble

fun FramesDocument.toFramesEntity(roundValues: Boolean = false): FramesEntity {
    return FramesEntity(
        soId = soId,
        areFramesNew = areFramesNew,
        design = design,
        reference = reference,
        value = value.roundToDouble(roundValues),
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}
