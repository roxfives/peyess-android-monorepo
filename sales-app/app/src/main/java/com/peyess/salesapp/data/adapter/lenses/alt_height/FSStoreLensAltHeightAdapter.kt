package com.peyess.salesapp.data.adapter.lenses.alt_height

import com.peyess.salesapp.data.model.lens.alt_height.FSStoreLensAltHeight
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument

fun FSStoreLensAltHeight.toStoreLensAltHeightDocument(
    id: String,
): StoreLensAltHeightDocument {
    return StoreLensAltHeightDocument(
        id = id,

        name = name,
        nameDisplay = name_display,
        value = value,
        observation = observation,

    )
}