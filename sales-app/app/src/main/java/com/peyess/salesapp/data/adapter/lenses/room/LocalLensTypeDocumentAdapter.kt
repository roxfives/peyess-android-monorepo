package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensTypeDocument

fun LocalLensTypeDocument.toLocalLensTypeEntity(): LocalLensTypeEntity {
    return LocalLensTypeEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}