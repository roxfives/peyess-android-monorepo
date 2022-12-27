package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterTypeImpl

fun StoreLensTypeDocument.toLensTypeModel(): LensFilterTypeImpl {
    return LensFilterTypeImpl(
        id = id,
        name = name,
    )
}