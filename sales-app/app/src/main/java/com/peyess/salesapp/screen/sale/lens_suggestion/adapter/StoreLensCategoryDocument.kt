package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensGroupDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterGroupImpl

fun StoreLensGroupDocument.toLensGroupModel(): LensFilterGroupImpl {
    return LensFilterGroupImpl(
        id = id,
        name = name,
    )
}