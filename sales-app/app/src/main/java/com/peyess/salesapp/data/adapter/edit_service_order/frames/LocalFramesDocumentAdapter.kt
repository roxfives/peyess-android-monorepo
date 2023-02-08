package com.peyess.salesapp.data.adapter.edit_service_order.frames

import com.peyess.salesapp.data.model.edit_service_order.frames.EditFramesDataEntity
import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument

fun LocalFramesDocument.toEditFramesDataEntity(): EditFramesDataEntity {
    return EditFramesDataEntity(
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
