package com.peyess.salesapp.screen.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesMaterialSimplified
import com.peyess.salesapp.screen.sale.lens_comparison.model.LensMaterial

fun LocalLensesMaterialSimplified.toLensMaterial(): LensMaterial {
    return LensMaterial(
        id = id,
        name = name,
    )
}
