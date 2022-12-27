package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesMaterialSimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensMaterialDocument

fun LocalLensesMaterialSimplified.toStoreLensMaterialDocument(): StoreLensMaterialDocument {
    return StoreLensMaterialDocument(
        id = id,
        name = name,
    )
}