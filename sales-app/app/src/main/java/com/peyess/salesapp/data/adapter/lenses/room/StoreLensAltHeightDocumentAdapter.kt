package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity

fun StoreLensAltHeightDocument.toLocalLensAltHeight(): LocalLensAltHeightEntity {
    return LocalLensAltHeightEntity(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        value = value,
        observation = observation,
    )
}