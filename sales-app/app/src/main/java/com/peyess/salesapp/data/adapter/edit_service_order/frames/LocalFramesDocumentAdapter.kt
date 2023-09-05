package com.peyess.salesapp.data.adapter.edit_service_order.frames

import com.peyess.salesapp.data.model.edit_service_order.frames.EditFramesDataEntity
import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument
import com.peyess.salesapp.utils.extentions.roundToDouble
import java.math.RoundingMode

fun LocalFramesDocument.toEditFramesDataEntity(
    roundValue: Boolean = false
): EditFramesDataEntity {
    return EditFramesDataEntity(
        soId = soId,
        areFramesNew = areFramesNew,
        description = design,
        reference = reference,
        value = value.roundToDouble(roundValue),
        tagCode = tagCode,
        type = type,
        framesInfo = framesInfo,
    )
}
