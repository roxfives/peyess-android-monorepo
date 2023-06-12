package com.peyess.salesapp.data.adapter.lenses.colorings

import com.peyess.salesapp.data.model.lens.coloring.FSStoreLensColoring
import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument

fun FSStoreLensColoring.toStoreColoringAdapter(
    id: String,
    supplierId: String,
): StoreLensColoringDocument {
    return StoreLensColoringDocument(
        id = id,
        brand = brand,
        cost = cost,
        price = price,
        design = design,
        display = display,
        explanations = explanations,
        hasMedical = hasMedical,
        isEnabled = isEnabled,
        isLocalEnabled = isLocalEnabled,
        isTreatmentRequired = isTreatmentRequired,
        observation = observation,
        priority = priority,
        shippingTime = shippingTime,
        specialty = specialty,
        suggestedPrice = suggestedPrice,
        supplierId = supplierId,
        supplier = supplier,
        type = type,
        warning = warning,
    )
}