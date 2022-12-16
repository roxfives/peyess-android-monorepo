package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensSpecialtyDocument

fun LocalLensSpecialtyDocument.toLocalLensSpecialtyEntity(): LocalLensSpecialtyEntity {
    return LocalLensSpecialtyEntity(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}