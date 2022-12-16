package com.peyess.salesapp.data.adapter.lenses.colorings

import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument

fun StoreLensColoringDocument.extractColoring():  LocalLensColoringDocument {
    return  LocalLensColoringDocument(
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
