package com.peyess.salesapp.screen.sale.frames.data.adapter

import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.features.frames.prescription_compatibility.model.PrescriptionCompatibility

fun LocalPrescriptionDocument.toPrescriptionCompatibility(): PrescriptionCompatibility {
    return PrescriptionCompatibility(
        sphericalLeft = sphericalLeft,
        sphericalRight = sphericalRight,
        cylindricalLeft = cylindricalLeft,
        cylindricalRight = cylindricalRight,
        axisLeft = axisLeft,
        axisRight = axisRight,
        hasAddition = hasAddition,
        additionLeft = additionLeft,
        additionRight = additionRight,
        hasPrism = hasPrism,
        prismDegreeLeft = prismDegreeLeft,
        prismDegreeRight = prismDegreeRight,
        prismAxisLeft = prismAxisLeft,
        prismAxisRight = prismAxisRight,
        prismPositionLeft = prismPositionLeft,
        prismPositionRight =prismPositionRight,
    )
}
