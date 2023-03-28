package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDescriptionDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterDescriptionImpl

fun StoreLensDescriptionDocument.toLensDescriptionModel(): LensFilterDescriptionImpl {
    return LensFilterDescriptionImpl(
        id = id,
        name = name,
    )
}