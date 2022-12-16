package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensGroupDocument

fun LocalLensGroupDocument.toLocalLensGroupEntity(): LocalLensGroupEntity {
    return LocalLensGroupEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}