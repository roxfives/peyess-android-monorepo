package com.peyess.salesapp.screen.edit_service_order.service_order.adapter

import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.feature.service_order.model.Prescription

fun LocalPrescriptionDocument.toPrescription(): Prescription {
    return Prescription(
        id = id,
        soId = soId,
        pictureUri = pictureUri,
        professionalName = professionalName,
        professionalId = professionalId,
        lensTypeCategory = lensTypeCategory,
        isCopy = isCopy,
        prescriptionDate = prescriptionDate,
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
    )
}
