package com.peyess.salesapp.screen.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDescriptionDocument
import com.peyess.salesapp.screen.sale.lens_pick.model.LensFilterDescriptionImpl

fun StoreLensDescriptionDocument.toLensDescriptionModel(): LensFilterDescriptionImpl {
    return LensFilterDescriptionImpl(
        id = id,
        name = name,
    )
}