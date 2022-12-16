package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensCategoryDocument

fun LocalLensCategoryEntity.toLocalLensCategoryDocument(): LocalLensCategoryDocument {
    return LocalLensCategoryDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}