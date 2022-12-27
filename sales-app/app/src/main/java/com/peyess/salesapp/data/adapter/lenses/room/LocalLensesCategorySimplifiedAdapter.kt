package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesGroupSimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensGroupDocument

fun LocalLensesGroupSimplified.toStoreLensGroupDocument(): StoreLensGroupDocument {
    return StoreLensGroupDocument(
        id = id,
        name = name,
    )
}