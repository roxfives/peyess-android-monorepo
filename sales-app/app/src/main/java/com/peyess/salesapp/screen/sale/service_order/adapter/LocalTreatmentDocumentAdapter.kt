package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.screen.sale.service_order.model.Treatment

fun LocalLensTreatmentDocument.toTreatment(): Treatment {
    return Treatment(
        id = id,
        brand = brand,
        price = price,
        isColoringRequired = isColoringRequired,
        design = design,
        observation = observation,
        priority = priority,
        shippingTime = shippingTime,
        specialty = specialty,
        supplier = supplier,
        warning = warning,
        explanations = explanations,
    )
}
