package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensFamilyDocument

fun LocalLensFamilyEntity.toLocalLensFamilyDocument(): LocalLensFamilyDocument {
    return LocalLensFamilyDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}