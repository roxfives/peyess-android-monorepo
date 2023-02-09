package com.peyess.salesapp.data.adapter.edit_service_order.frames

import com.peyess.salesapp.data.model.edit_service_order.frames.EditFramesDataEntity
import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument

fun EditFramesDataEntity.toLocalFramesDocument(): LocalFramesDocument {
    return LocalFramesDocument(
        soId = soId,
        areFramesNew = areFramesNew,
        design = description,
        reference = reference,
        value = value,
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}