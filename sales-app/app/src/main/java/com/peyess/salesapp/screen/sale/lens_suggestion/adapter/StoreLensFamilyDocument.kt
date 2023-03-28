package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensFamilyDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterFamilyImpl

fun StoreLensFamilyDocument.toLensFamilyModel(): LensFilterFamilyImpl {
    return LensFilterFamilyImpl(
        id = id,
        name = name,
    )
}