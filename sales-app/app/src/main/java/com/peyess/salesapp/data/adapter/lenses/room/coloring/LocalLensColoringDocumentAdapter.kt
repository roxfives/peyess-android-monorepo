package com.peyess.salesapp.data.adapter.lenses.room.coloring

import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity

fun LocalLensColoringDocument.toLocalLensColoringEntity(): LocalLensColoringEntity {
    return LocalLensColoringEntity(
        id = id,
        brand = brand,
        price = price,
        design = design,
        hasMedical = hasMedical,
        isEnabled = isEnabled,
        isLocalEnabled = isLocalEnabled,
        isTreatmentRequired = isTreatmentRequired,
        observation = observation,
        priority = priority,
        shippingTime = shippingTime,
        specialty = specialty,
        supplierId = supplierId,
        supplier = supplier,
        type = type,
        warning = warning,
    )
}