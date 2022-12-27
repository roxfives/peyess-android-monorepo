package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesFamilySimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensFamilyDocument

fun LocalLensesFamilySimplified.toStoreLensFamilyDocument(): StoreLensFamilyDocument {
    return StoreLensFamilyDocument(
        id = id,
        name = name,
    )
}