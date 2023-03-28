package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensSpecialtyDocument

fun LocalLensSpecialtyEntity.toLocalLensSpecialtyDocument(): LocalLensSpecialtyDocument {
    return LocalLensSpecialtyDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}