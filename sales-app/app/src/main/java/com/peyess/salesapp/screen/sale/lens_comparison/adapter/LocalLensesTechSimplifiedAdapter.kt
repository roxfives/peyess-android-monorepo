package com.peyess.salesapp.screen.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesTechSimplified
import com.peyess.salesapp.feature.lens_comparison.model.LensTech

fun LocalLensesTechSimplified.toLensTech(): LensTech {
    return LensTech(
        id = id,
        name = name,
    )
}
