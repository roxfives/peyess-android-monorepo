package com.peyess.salesapp.screen.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensGroupDocument
import com.peyess.salesapp.screen.sale.lens_pick.model.LensFilterGroupImpl

fun StoreLensGroupDocument.toLensGroupModel(): LensFilterGroupImpl {
    return LensFilterGroupImpl(
        id = id,
        name = name,
    )
}