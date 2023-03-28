package com.peyess.salesapp.data.adapter.lenses.treatments

import com.peyess.salesapp.data.model.lens.treatment.FSLensTreatment
import com.peyess.salesapp.data.model.lens.treatment.StoreLensTreatmentDocument

fun FSLensTreatment.toStoreLensTreatmentDocument(
    id: String,
): StoreLensTreatmentDocument {
    return StoreLensTreatmentDocument(
        id = id,
        brand = brand,
        cost = cost,
        price = price,
        design = design,
        explanations = explanations,
        isColoringRequired = isColoringRequired,
        isEnabled = isEnabled,
        isLocalEnabled = isLocalEnabled,
        observation = observation,
        priority = priority,
        shippingTime = shippingTime,
        specialty = specialty,
        suggestedPrice = suggestedPrice,
        supplier = supplier,
        warning = warning,
    )
}