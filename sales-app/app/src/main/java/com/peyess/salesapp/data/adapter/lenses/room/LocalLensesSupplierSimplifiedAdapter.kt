package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesSupplierSimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSupplierDocument

fun LocalLensesSupplierSimplified.toStoreLensSupplierDocument(): StoreLensSupplierDocument {
    return StoreLensSupplierDocument(
        id = id,
        name = name,
    )
}