package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSupplierDocument
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterSupplierImpl

fun StoreLensSupplierDocument.toLensSupplierModel(): LensFilterSupplierImpl {
    return LensFilterSupplierImpl(
        id = id,
        name = name,
    )
}