package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensFamilyDocument

fun LocalLensFamilyDocument.toLocalLensFamilyEntity(): LocalLensFamilyEntity {
    return LocalLensFamilyEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}