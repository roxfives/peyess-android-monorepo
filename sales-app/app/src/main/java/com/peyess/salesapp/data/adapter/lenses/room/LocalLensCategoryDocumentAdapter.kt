package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensCategoryDocument

fun LocalLensCategoryDocument.toLocalLensCategoryEntity(): LocalLensCategoryEntity {
    return LocalLensCategoryEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}