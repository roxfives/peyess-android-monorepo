package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensMaterialDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterMaterialImpl

fun StoreLensMaterialDocument.toLensMaterialModel(): LensFilterMaterialImpl {
    return LensFilterMaterialImpl(
        id = id,
        name = name,
    )
}