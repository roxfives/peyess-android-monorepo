package com.peyess.salesapp.screen.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.feature.lens_comparison.model.Treatment

fun LocalLensTreatmentDocument.toTreatment(): Treatment {
    return Treatment(
        id = id,
        brand = brand,
        price = price,
        design = design,
        isColoringRequired = isColoringRequired,
        isEnabled = isEnabled,
        isLocalEnabled = isLocalEnabled,
        observation = observation,
        priority = priority,
        shippingTime = shippingTime,
        specialty = specialty,
        supplier = supplier,
        warning = warning,
        explanations = explanations,
    )
}
