package com.peyess.salesapp.screen.sale.prescription_picture.adapter

import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.screen.sale.prescription_picture.model.PrescriptionPicture

fun PrescriptionPicture.toLocalPrescriptionDocument(): LocalPrescriptionDocument {
    return LocalPrescriptionDocument(
        id = id,
        soId = soId,
        pictureUri = pictureUri,
        professionalName = professionalName,
        professionalId = professionalId,
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