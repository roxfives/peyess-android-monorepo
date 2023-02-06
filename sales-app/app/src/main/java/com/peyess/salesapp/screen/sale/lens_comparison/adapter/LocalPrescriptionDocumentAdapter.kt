package com.peyess.salesapp.screen.sale.lens_comparison.adapter

import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.screen.sale.lens_comparison.model.Prescription

fun LocalPrescriptionDocument.toPrescription(): Prescription {
    return Prescription(
        soId = soId,
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
        prismPositionRight = prismPositionRight,
        pictureUri = pictureUri,
        professionalName = professionalName,
        professionalId = professionalId,
        isCopy = isCopy,
        prescriptionDate = prescriptionDate,
    )
}