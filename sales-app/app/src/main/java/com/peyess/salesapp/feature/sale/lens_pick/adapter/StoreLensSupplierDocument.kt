package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSupplierDocument
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterSupplierImpl

fun StoreLensSupplierDocument.toLensSupplierModel(): LensFilterSupplierImpl {
    return LensFilterSupplierImpl(
        id = id,
        name = name,
    )
}