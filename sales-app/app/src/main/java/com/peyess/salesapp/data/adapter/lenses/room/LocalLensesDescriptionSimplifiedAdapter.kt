package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesDescriptionSimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDescriptionDocument

fun LocalLensesDescriptionSimplified.toStoreLensDescriptionDocument(): StoreLensDescriptionDocument {
    return StoreLensDescriptionDocument(
        id = id,
        name = name,
    )
}