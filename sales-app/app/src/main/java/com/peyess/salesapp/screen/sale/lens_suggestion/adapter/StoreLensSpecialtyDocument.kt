package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSpecialtyDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterSpecialtyImpl

fun StoreLensSpecialtyDocument.toLensSpecialtyModel(): LensFilterSpecialtyImpl {
    return LensFilterSpecialtyImpl(
        id = id,
        name = name,
    )
}