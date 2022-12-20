package com.peyess.salesapp.data.adapter.disponibilities

import com.peyess.salesapp.data.model.lens.disponibility.FSDispManufacturer
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensDisponibilityManufacturerDocument

fun FSDispManufacturer.toDispManufacturerDocument(id: String): StoreLensDisponibilityManufacturerDocument {
    return StoreLensDisponibilityManufacturerDocument(
        id = id,
        name = name,
        shippingTime = shippingTime,
    )
}