package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensSupplierDocument

fun LocalLensSupplierDocument.toLocalLensSupplierEntity(): LocalLensSupplierEntity {
    return LocalLensSupplierEntity(
        id = id,
        name = name,
        picture = picture,
        priority = priority,
        storePriority = storePriority,
    )
}