package com.peyess.salesapp.data.adapter.local_sale.prescription

import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument

fun PrescriptionEntity.toLocalPrescriptionDocument(): LocalPrescriptionDocument {
    return LocalPrescriptionDocument(
        id = id,

        soId = soId,

        professionalName = professionalName,
        professionalId = professionalId,
        isCopy = isCopy,
        prescriptionDate = prescriptionDate,
        pictureUri = pictureUri,

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
