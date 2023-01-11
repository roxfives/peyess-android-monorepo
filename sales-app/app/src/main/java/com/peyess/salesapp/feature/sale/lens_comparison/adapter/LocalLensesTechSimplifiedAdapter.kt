package com.peyess.salesapp.feature.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesTechSimplified
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensTech

fun LocalLensesTechSimplified.toLensTech(): LensTech {
    return LensTech(
        id = id,
        name = name,
    )
}
