package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialCategoryEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensMaterialCategoryDocument

fun LocalLensMaterialCategoryEntity.toLocalLensMaterialCategoryDocument(): LocalLensMaterialCategoryDocument {
    return LocalLensMaterialCategoryDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}