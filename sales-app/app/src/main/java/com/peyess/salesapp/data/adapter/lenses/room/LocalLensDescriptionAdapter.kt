package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensDescriptionDocument

fun LocalLensDescriptionDocument.toLocalLensDescriptionEntity(): LocalLensDescriptionEntity {
    return LocalLensDescriptionEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}