package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensMaterialDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterMaterialImpl

fun StoreLensMaterialDocument.toLensMaterialModel(): LensFilterMaterialImpl {
    return LensFilterMaterialImpl(
        id = id,
        name = name,
    )
}