package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensTechDocument

fun LocalLensTechEntity.toLocalLensTechDocument(): LocalLensTechDocument {
    return LocalLensTechDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}