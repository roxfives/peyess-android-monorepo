package com.peyess.salesapp.data.adapter.lenses.room.treatment

import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentEntity

fun LocalLensTreatmentEntity.toLocalLensTreatmentDocument(
    explanations: List<String>,
): LocalLensTreatmentDocument {
    return LocalLensTreatmentDocument(
        id = id,
        brand = brand,
        price = price,
        design = design,
        isEnabled = isEnabled,
        isLocalEnabled = isLocalEnabled,
        isColoringRequired = isColoringRequired,
        observation = observation,
        priority = priority,
        shippingTime = shippingTime,
        specialty = specialty,
        supplierId = supplierId,
        supplier = supplier,
        warning = warning,

        explanations = explanations,
    )
}