package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterTypeImpl

fun StoreLensTypeDocument.toLensTypeModel(): LensFilterTypeImpl {
    return LensFilterTypeImpl(
        id = id,
        name = name,
    )
}