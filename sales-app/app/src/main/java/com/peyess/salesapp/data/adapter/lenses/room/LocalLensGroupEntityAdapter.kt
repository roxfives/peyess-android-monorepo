package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensGroupDocument

fun LocalLensGroupEntity.toLocalLensGroupDocument(): LocalLensGroupDocument {
    return LocalLensGroupDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}