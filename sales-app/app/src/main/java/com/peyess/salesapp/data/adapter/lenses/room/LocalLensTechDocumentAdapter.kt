package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensTechDocument

fun LocalLensTechDocument.toLocalLensTechEntity(): LocalLensTechEntity {
    return LocalLensTechEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}