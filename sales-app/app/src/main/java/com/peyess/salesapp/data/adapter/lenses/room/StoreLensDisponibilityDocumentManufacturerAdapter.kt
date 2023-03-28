package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityManufacturerEntity
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityManufacturerDocument

fun StoreLensDisponibilityManufacturerDocument
        .toLocalLensDisponibilityManufacturerEntity(): LocalLensDisponibilityManufacturerEntity {
    return LocalLensDisponibilityManufacturerEntity(
        id = id,
        name = name,
        shippingTime = shippingTime,
    )
}
