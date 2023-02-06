package com.peyess.salesapp.screen.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.screen.sale.lens_comparison.model.Coloring

fun LocalLensColoringDocument.toColoring(): Coloring {
    return Coloring(
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
        supplier = supplier,
        type = type,
        warning = warning,
        explanations = explanations,
    )
}