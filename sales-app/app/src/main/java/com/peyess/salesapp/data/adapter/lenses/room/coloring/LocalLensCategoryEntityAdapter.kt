package com.peyess.salesapp.data.adapter.lenses.room.coloring

import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity


fun LocalLensColoringEntity.toLocalLensColoringDocument(
    explanations: List<String>,
): LocalLensColoringDocument {
    return LocalLensColoringDocument(
        id = id,
        brand = brand,
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

        explanations = explanations,
    )
}