package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensMaterialDocument

fun LocalLensMaterialEntity.toLocalLensMaterialDocument(): LocalLensMaterialDocument {
    return LocalLensMaterialDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
        n = n,
        observation = observation,
        categoryId = categoryId,
        category = category,
    )
}