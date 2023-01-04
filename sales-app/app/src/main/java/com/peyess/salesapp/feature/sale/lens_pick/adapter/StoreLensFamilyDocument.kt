package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensFamilyDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterFamilyImpl

fun StoreLensFamilyDocument.toLensFamilyModel(): LensFilterFamilyImpl {
    return LensFilterFamilyImpl(
        id = id,
        name = name,
    )
}