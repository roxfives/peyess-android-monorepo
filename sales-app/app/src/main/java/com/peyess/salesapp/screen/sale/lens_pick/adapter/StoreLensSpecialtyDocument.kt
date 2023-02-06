package com.peyess.salesapp.screen.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSpecialtyDocument
import com.peyess.salesapp.screen.sale.lens_pick.model.LensFilterSpecialtyImpl

fun StoreLensSpecialtyDocument.toLensSpecialtyModel(): LensFilterSpecialtyImpl {
    return LensFilterSpecialtyImpl(
        id = id,
        name = name,
    )
}