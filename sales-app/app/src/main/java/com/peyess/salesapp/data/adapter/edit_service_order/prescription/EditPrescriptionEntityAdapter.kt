package com.peyess.salesapp.data.adapter.edit_service_order.prescription

import com.peyess.salesapp.data.model.edit_service_order.prescription.EditPrescriptionEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument

fun EditPrescriptionEntity.toLocalPrescriptionDocument(): LocalPrescriptionDocument {
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
    )
}
