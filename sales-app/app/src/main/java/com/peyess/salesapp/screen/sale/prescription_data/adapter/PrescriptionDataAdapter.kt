package com.peyess.salesapp.screen.sale.prescription_data.adapter

import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.feature.prescription.model.PrescriptionData

fun PrescriptionData.toLocalPrescriptionDocument(): LocalPrescriptionDocument {
    return LocalPrescriptionDocument(
        id = id,
        soId = soId,

        pictureUri = pictureUri,
        professionalName = professionalName,
        professionalId = professionalId,
        isCopy = isCopy,
        prescriptionDate = prescriptionDate,

        lensTypeCategoryId = lensTypeCategoryId,
        lensTypeCategory = lensTypeCategory,

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

        observation = observation,
    )
}