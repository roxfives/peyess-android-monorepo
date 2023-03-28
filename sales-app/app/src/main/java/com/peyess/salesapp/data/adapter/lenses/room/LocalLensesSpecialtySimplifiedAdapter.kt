package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesSpecialtySimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensSpecialtyDocument

fun LocalLensesSpecialtySimplified.toStoreLensSpecialtyDocument(): StoreLensSpecialtyDocument {
    return StoreLensSpecialtyDocument(
        id = id,
        name = name,
    )
}