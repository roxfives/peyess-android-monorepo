package com.peyess.salesapp.data.adapter.lenses.alt_height

import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity

fun LocalLensAltHeightEntity.toStoreLensAltHeightDocument(): StoreLensAltHeightDocument {
    return StoreLensAltHeightDocument(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
        value = value,
        observation = observation,
    )
}