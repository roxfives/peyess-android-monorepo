package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensSupplierDocument

fun LocalLensSupplierEntity.toLocalLensSupplierDocument(): LocalLensSupplierDocument {
    return LocalLensSupplierDocument(
        id = id,
        name = name,
        priority = priority,
        storePriority = storePriority,
    )
}