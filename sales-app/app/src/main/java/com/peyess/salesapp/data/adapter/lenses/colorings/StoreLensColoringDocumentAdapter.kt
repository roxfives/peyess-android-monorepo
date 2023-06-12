package com.peyess.salesapp.data.adapter.lenses.colorings

import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentDocument
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentEntity
import com.peyess.salesapp.data.model.lens.treatment.StoreLensTreatmentDocument

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
        supplierId = supplierId,
        supplier = supplier,
        type = type,
        warning = warning,
        explanations = explanations,
    )
}

fun StoreLensTreatmentDocument.extractTreatment():  LocalLensTreatmentDocument {
    return  LocalLensTreatmentDocument(
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
