package com.peyess.salesapp.feature.sale.service_order.adapter

import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.feature.sale.service_order.model.Coloring

fun LocalLensColoringDocument.toColoring(): Coloring {
    return Coloring(
        id = id,
        brand = brand,
        price = price,
        design = design,
        hasMedical = hasMedical,
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